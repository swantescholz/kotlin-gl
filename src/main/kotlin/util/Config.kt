package util

import util.log.Log
import java.io.File
import java.util.*

class Config(val configFile: File) {
	
	private val settings = LinkedHashMap<String, String>()
	
	init {
		reload()
	}
	
	fun reload() {
		settings.clear()
		try {
			configFile.forEachLine { line ->
				if (line.isEmpty() || line[0] == '#')
					return@forEachLine
				val equalsIndex = line.indexOfFirst { it == '=' }
				if (equalsIndex == -1) {
					Log.warn("config line '$line' contains no '='! ignored")
					return@forEachLine
				}
				val key = line.substring(0, equalsIndex)
				val value = line.substring(equalsIndex + 1)
				if (key in settings) {
					Log.warn("config line '$line' overrides previous setting of '$key'!")
				}
				settings[key] = value
			}
		} catch (e: Exception) {
			Log.errorAndThrow(e)
		}
	}
	
	operator fun get(key: String): String {
		astIn(key, settings.keys)
		return settings[key]!!
	}
	
	// requires key to be set already
	operator fun set(key: String, value: String) {
		astIn(key, settings.keys)
		settings[key] = value
	}
	
	fun add(key: String, value: String) {
		astNotIn(key, settings.keys)
		settings[key] = value
	}
	
	operator fun contains(key: String): Boolean = key in settings
	
	fun writeToFile(outputFile: File = configFile) {
		val sb = StringBuilder()
		for ((key, value) in settings) {
			sb.append("$key=$value\n")
		}
		configFile.writeText(sb.toString())
	}
	
	companion object {
		val game = Config(File("res/config.txt"))
	}
	
}