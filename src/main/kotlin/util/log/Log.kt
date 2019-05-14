package util.log

import gl.util.StringUtil
import util.stacktrace
import java.io.*
import java.util.*


object Log {
	
	private val listeners = ArrayList<Listener>()
	
	private class Listener(val stream: OutputStream, val logLevel: LogLevel)
	
	@Synchronized private fun addListener(listener: Listener) {
		listeners.add(listener)
	}
	
	fun addListener(stream: OutputStream, logLevel: LogLevel) {
		addListener(Listener(stream, logLevel))
	}
	
	@Throws(FileNotFoundException::class)
	fun addOutputFile(outputFile: File, logLevel: LogLevel) {
		val stream = FileOutputStream(outputFile, true)
		addListener(stream, logLevel)
	}
	
	@Synchronized private fun log(message: String, logLevel: LogLevel) {
		val bytes = (logLevel.prefix + message + StringUtil.NEWLINE).toByteArray()
		listeners.forEach { it ->
			if (!it.logLevel.allowsToLog(logLevel)) {
				return@forEach
			}
			try {
				it.stream.write(bytes)
				it.stream.flush()
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
	}
	
	fun debug(message: String) {
		log(message, LogLevel.DEBUG)
	}
	
	fun info(message: String) {
		log(message, LogLevel.INFO)
	}
	
	fun warn(message: String) {
		log(message, LogLevel.WARN)
	}
	
	fun warn(e: Exception) {
		log(e.myString, LogLevel.WARN)
	}
	
	fun error(message: String) {
		log(message, LogLevel.ERROR)
	}
	
	fun errorAndExit(message: String) {
		log(message, LogLevel.ERROR)
		System.exit(-1)
	}
	
	fun errorAndThrow(e: Exception) {
		log(e.myString, LogLevel.ERROR)
		throw e
	}
	
	fun errorAndThrow(message: String) {
		log(message, LogLevel.ERROR)
		throw RuntimeException(message)
	}
	
	private val Exception.myString: String
		get() = "$this\n$stacktrace"
	
	
}
