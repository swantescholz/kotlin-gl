package util

import math.NANO
import kotlin.system.exitProcess

val HOME: String = System.getProperty("user.home")

inline fun ignoreExceptions(task: () -> Unit) {
	try {
		task()
	} catch (e: Exception) {
	}
}

fun sleep(seconds: Double) {
	try {
		Thread.sleep((1000 * seconds).toLong())
	} catch(ex: InterruptedException) {
		Thread.currentThread().interrupt()
	}
}

fun quit() {
	exitProcess(-1)
}

fun <R> stage(name: String = "<unknown>", printInfo: Boolean = true, work: () -> R): R {
	if (printInfo) {
		util.string.println("stage $name: init")
	}
	val res = work()
	if (printInfo) {
		util.string.println("stage $name: done")
	}
	return res
}

private var _print_regularly_last_time = System.nanoTime()
fun printlnRegularly(vararg args: Any, dt: Double = 3.0) {
	val now = System.nanoTime()
	if ((now - _print_regularly_last_time) * NANO > dt) {
		_print_regularly_last_time = now
		var s = ""
		for (it in args) {
			s += it.toString() + " "
		}
		System.out.println(s.removeSuffix(" "))
	}
}

private var _do_regularly_last_time = System.nanoTime()
fun doRegularly(dt: Double = 5.0, function: () -> Unit) {
	val now = System.nanoTime()
	if ((now - _do_regularly_last_time) * NANO > dt) {
		_do_regularly_last_time = now
		function()
	}
}

private var _doEveryNTimesCounter = -1L
fun doEveryNTimes(n: Long = 10000, function: () -> Unit) {
	if (_doEveryNTimesCounter < 0)
		_doEveryNTimesCounter = n
	_doEveryNTimesCounter--
	if (_doEveryNTimesCounter == 0L) {
		_doEveryNTimesCounter = -1
		function()
	}
}

private var _quitAfterTimesCounter = -1
fun quitAfterTimes(ntimes: Int) {
	if (_quitAfterTimesCounter < 0)
		_quitAfterTimesCounter = ntimes
	_quitAfterTimesCounter--
	if (_quitAfterTimesCounter == 0) {
		System.exit(0)
	}
}