package util.log

import gl.util.StringUtil
import java.time.LocalTime

enum class LogLevel private constructor(private val level: Int) {
	
	DEBUG(1),
	INFO(2),
	WARN(3),
	ERROR(4);
	
	fun allowsToLog(applyingLog: LogLevel): Boolean {
		return this.level <= applyingLog.level
	}
	
	val prefix: String
		get() = timestamp + " [" + toString() + "] "
	
	internal val timestamp: String
		get() {
			val t = LocalTime.now()
			val f = { number: Int -> StringUtil.fillZeroes(number, 2) }
			val sep = ":"
			return f(t.hour) + sep + f(t.minute) + sep + f(t.second)
		}
}
