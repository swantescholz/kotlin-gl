package util.extensions

import math.LOG10
import math.LOG2
import math.POWS2
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

fun Int.cube() = this * this * this
fun Int.seqSetBits(): Sequence<Int> {
	var i = 0
	var x = this
	return generateSequence {
		while (x % 2 == 0) {
			x /= 2
			i++
			if (i >= 30)
				return@generateSequence null
		}
		x /= 2
		i++
		i - 1
	}
}

fun Int.abs(): Int = Math.abs(this)
fun Int.unsetBit(nthLowestBitIndex: Int): Int = this.setBit(nthLowestBitIndex, false)
fun Int.setBit(nthLowestBitIndex: Int, value: Boolean = true): Int {
	if (value)
		return this or (1 shl nthLowestBitIndex)
	return this and (1 shl nthLowestBitIndex).inv()
}

fun Int.getBit(nthLowestBitIndex: Int): Boolean {
	return 0 != (this and (1 shl nthLowestBitIndex))
}


fun Int.toBitString(): String {
	return Integer.toBinaryString(this)!!
}

fun Int.repunit(): Long = POWS2[this] - 1
fun Int.bitLength(): Int {
	for (i in 31 downTo 1) {
		if (this and (1 shl i) != 0)
			return i + 1
	}
	return 1
}

fun Int.binaryConcat(you: Int): Int {
	return this * (1 shl you.bitLength()) + you
}

fun Int.big(): BigInteger {
	return BigInteger.valueOf(this.toLong())
}

fun Int.bigd(): BigDecimal {
	return BigDecimal.valueOf(this.toLong())
}

inline fun Int.doTimes(func: () -> Unit) {
	for (i in 1..this) {
		func()
	}
}

tailrec fun Int.gcd(you: Int): Int {
	if (you == 0)
		return this
	return you.gcd(this % you)
}


fun Int.lcm(you: Int) = (this * you) / this.gcd(you)

fun Int.square(): Long = this.toLong() * this

fun Int.ln(): Double {
	return Math.log(this.toDouble())
}

fun Int.log(base: Int): Double {
	return this.ln() / base.ln()
}

fun Int.log2(): Double {
	return this.ln() / LOG2
}

fun Int.log10(): Double {
	return this.ln() / LOG10
}

fun Int.repeat(length: Int): List<Int> {
	val al = ArrayList<Int>(length)
	for (it in 1..length)
		al.add(this)
	return al
}

fun Int.sqrt(): Double {
	return Math.sqrt(this.toDouble())
}

fun Int.even(): Boolean {
	return this % 2 == 0
}

fun Int.odd(): Boolean {
	return this % 2 == 1
}

infix fun Int.diff(you: Int): Int {
	return Math.abs(this - you)
}


fun Int.min(you: Int): Int {
	return Math.min(this, you)
}

fun Int.min(you: Long): Long {
	return Math.min(this.toLong(), you)
}

fun Int.max(you: Int): Int {
	return Math.max(this, you)
}

fun Int.max(you: Long): Long {
	return Math.max(this.toLong(), you)
}
