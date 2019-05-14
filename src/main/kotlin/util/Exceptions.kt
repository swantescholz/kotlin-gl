package util

import java.io.PrintWriter
import java.io.StringWriter

class NotImplementedException : RuntimeException("Not yet implemented. RuntimeException thrown")

val Exception.stacktrace: String
	get() = {
		val sw = StringWriter()
		val pw = PrintWriter(sw)
		this.printStackTrace(pw)
		sw.toString()
	}()