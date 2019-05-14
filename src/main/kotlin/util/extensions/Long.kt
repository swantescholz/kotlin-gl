package util.extensions

import math.LOG10
import math.LOG2
import math.POWS2
import util.astGreaterEqual
import java.math.BigDecimal
import java.math.BigInteger

fun Long.signum(): Long {
	if (this > 0)
		return 1
	if (this < 0)
		return -1
	return 0
}

fun Long.cube() = this * this * this
fun Long.modMul(you: Long, mod: Long): Long {
	// inefficient
	val a = this % mod
	val b = you % mod
	if (a.abs() < Int.MAX_VALUE && b.abs() < Int.MAX_VALUE) {
		return (a * b) % mod
	}
	return a.big().multiply(b.big()).mod(mod).toLong()
}

fun Long.floorSqrt(): Long {
	val x = this
	if (x < 0x10000000000000L) return StrictMath.sqrt(x.toDouble()).toLong()
	val result = StrictMath.sqrt(2.0 * x.ushr(1)).toLong()
	val res = if (result * result - x > 0L) result.toInt() - 1L else result.toLong()
	astGreaterEqual(res, 0L)
	return res
}

fun Long.abs(): Long = Math.abs(this)

fun Long.modPow(exponent: Long, m: Long): Long {
	return this.big().modPow(exponent.big(), m.big()).toLong()
}

fun Long.repunit(): Long = POWS2[this.toInt()] - 1
fun Long.bitLength(): Int {
	for (i in 31 downTo 1) {
		if (this and POWS2[i] != 0L)
			return i + 1
	}
	return 1
}

fun Long.binaryConcat(you: Long): Long {
	return this * POWS2[you.bitLength()] + you
}

fun Long.toBitString(): String {
	return BigInteger.valueOf(this).toString(2)
}

fun Long.divmod(d: Long): Pair<Long, Long> {
	return Pair(this / d, this % d)
}

fun Long.big(): BigInteger {
	return BigInteger.valueOf(this)
}

fun Long.bigd(): BigDecimal {
	return BigDecimal.valueOf(this)
}

fun Long.ln(): Double {
	return Math.log(this.toDouble())
}

fun Long.log(base: Int): Double {
	return this.ln() / base.ln()
}

fun Long.log2(): Double {
	return this.ln() / LOG2
}

fun Long.log10(): Double {
	return this.ln() / LOG10
}

inline fun Long.doTimes(func: () -> Unit) {
	for (i in 1L..this) {
		func()
	}
}


fun Long.odd(): Boolean {
	return this % 2 == 1L
}

fun Long.even(): Boolean {
	return this % 2 == 0L
}

infix fun Long.diff(you: Long): Long {
	return Math.abs(this - you)
}


fun Long.min(you: Long): Long {
	return Math.min(this, you)
}

fun Long.max(you: Long): Long {
	return Math.max(this, you)
}

fun Long.square(): Long = this * this

fun Int.pow(exp: Int) = this.toLong().pow(exp.toLong())
fun Int.pow(exp: Long) = this.toLong().pow(exp)
fun Long.pow(exp: Int) = this.pow(exp.toLong())

fun Long.pow(exp: Long): Long {
	if (exp < 1)
		return 1
	if (this == 1L)
		return 1
	if (this == -1L) {
		if (exp.even())
			return 1L
		return -1L
	}
	if (this == 2L)
		return 1L shl exp.toInt()
	var e = exp
	var y = 1L
	var res = this
	while (e > 1) {
		if (e.odd())
			y *= res
		res *= res
		e /= 2
	}
	return res * y
}

fun Long.sqrt(): Double {
	return Math.sqrt(this.toDouble())
}


fun Long.unsetBit(nthLowestBitIndex: Int): Long = this.setBit(nthLowestBitIndex, false)
fun Long.setBit(nthLowestBitIndex: Int, value: Boolean = true): Long {
	if (value)
		return this or (1L shl nthLowestBitIndex)
	return this and (1L shl nthLowestBitIndex).inv()
}

fun Long.getBit(nthLowestBitIndex: Int): Boolean {
	return 0L != (this and (1L shl nthLowestBitIndex))
}