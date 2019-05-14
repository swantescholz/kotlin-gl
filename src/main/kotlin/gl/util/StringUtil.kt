package gl.util

import java.text.SimpleDateFormat
import java.util.*

object StringUtil {
	
	val NEWLINE = "\n"
	
	fun currentTimestamp(): String {
		val date = Date()
		val sdf = SimpleDateFormat("yyMMdd-HHmmss")
		return sdf.format(date)
	}
	
	
	fun countSubstring(str: String, substr: String): Int {
		var lastIndex = 0
		var count = 0
		while (lastIndex != -1) {
			lastIndex = str.indexOf(substr, lastIndex)
			if (lastIndex != -1) {
				count++
				lastIndex += substr.length
			}
		}
		return count
	}
	
	fun fillZeroes(number: Int, minLength: Int): String {
		var s = "" + number
		while (s.length < minLength) {
			s = "0" + s
		}
		return s
	}
}
