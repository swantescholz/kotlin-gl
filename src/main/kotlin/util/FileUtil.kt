package util

import util.log.Log

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

fun Sequence<String>.writeLinesToFile(path: Path) {
	val writer = Files.newBufferedWriter(path)
	for (line in this) {
		writer.write("$line\n")
	}
	writer.close()
}

object FileUtil {
	
	fun readFile(file: File): String {
		
		try {
			val br = BufferedReader(FileReader(file))
			val sb = StringBuilder()
			var line: String? = br.readLine()
			
			while (line != null) {
				sb.append(line)
				sb.append('\n')
				line = br.readLine()
			}
			br.close()
			return sb.toString()
		} catch (e: IOException) {
			Log.warn(e)
		}
		
		return ""
	}
	
	// reads lines and maps them until map returns null
	fun <T : Any> seqLinesOfFile(path: Path, map: (String) -> T?): Sequence<T> {
		val reader = Files.newBufferedReader(path)
		return generateSequence {
			map(reader.readLine())
			
		}
	}
}