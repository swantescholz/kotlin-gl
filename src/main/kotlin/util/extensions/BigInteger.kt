package util.extensions

import math.*
import util.astGreaterEqual
import util.astLess
import java.math.BigInteger

operator fun Int.plus(you: BigInteger): BigInteger = this.big() + you
operator fun Int.minus(you: BigInteger): BigInteger = this.big() - you
operator fun Int.times(you: BigInteger): BigInteger = this.big() * you
operator fun Int.div(you: BigInteger): BigInteger = this.big() / you
operator fun Int.mod(you: BigInteger): BigInteger = this.big() % you
operator fun Long.plus(you: BigInteger): BigInteger = this.big() + you
operator fun Long.minus(you: BigInteger): BigInteger = this.big() - you
operator fun Long.times(you: BigInteger): BigInteger = this.big() * you
operator fun Long.div(you: BigInteger): BigInteger = this.big() / you
operator fun Long.mod(you: BigInteger): BigInteger = this.big() % you
operator fun BigInteger.plus(you: Int): BigInteger = this + you.big()
operator fun BigInteger.minus(you: Int): BigInteger = this - you.big()
operator fun BigInteger.times(you: Int): BigInteger = this * you.big()
operator fun BigInteger.div(you: Int): BigInteger = this / you.big()
operator fun BigInteger.mod(you: Int): BigInteger = this % you.big()
operator fun BigInteger.plus(you: Long): BigInteger = this + you.big()
operator fun BigInteger.minus(you: Long): BigInteger = this - you.big()
operator fun BigInteger.times(you: Long): BigInteger = this * you.big()
operator fun BigInteger.div(you: Long): BigInteger = this / you.big()
operator fun BigInteger.mod(you: Long): BigInteger = this % you.big()

fun BigInteger.factorial(): BigInteger {
	val exp = this.toInt()
	astLess(exp, 2000000)
	var res = 1.big()
	for (i in 2..exp) {
		res *= i.big()
	}
	return res
}

fun BigInteger.lcm(you: BigInteger): BigInteger {
	return (this * you) / this.gcd(you)
}

// choose r out of n
infix fun BigInteger.nCr(r: BigInteger): BigInteger {
	if (r < BIG0 || r > this)
		return BIG0
	if (r == BIG0 || r == this)
		return BIG1
	var res = BIG1
	var n = this
	var rvar = r
	if (rvar > n - rvar)
		rvar = n - rvar
	var ir = BIG2
	while (true) {
		if (ir > rvar)
			break
		while (res % ir != BIG0) {
			res *= n
			n -= BIG1
		}
		res /= ir
		ir += BIG1
	}
	while (n > this - rvar) {
		res *= n
		n -= BIG1
	}
	return res
}

fun BigInteger.isPalindrome(radix: Int = 10): Boolean {
	return this.toString(radix).isPalindrome()
}

fun BigInteger.reversed(radix: Int = 10): BigInteger {
	return BigInteger(this.toString(radix).reversed(), radix)
}

fun BigInteger.floorOfNthRoot(n: Int): BigInteger {
	val sign = this.signum()
	if (n <= 0 || (sign < 0))
		throw IllegalArgumentException()
	if (sign == 0)
		return BigInteger.ZERO
	if (n == 1)
		return this
	var a: BigInteger
	val bigN = BigInteger.valueOf(n.toLong())
	val bigNMinusOne = BigInteger.valueOf(n.toLong() - 1)
	var b = BigInteger.ZERO.setBit(1 + this.bitLength() / n)
	do {
		a = b
		b = a.multiply(bigNMinusOne).add(this.divide(a.pow(n - 1))).divide(bigN)
	} while (b.compareTo(a) == -1)
	return a
}

fun BigInteger.floorCbrt(): BigInteger {
	astGreaterEqual(this, BIG0)
	if (this <= BIG1)
		return this
	var x = BigInteger.ZERO.setBit(this.bitLength() / 3 + 1)
	while (true) {
		val y = x.shiftLeft(1).add(this.divide(x.multiply(x))).divide(BIG3)
		if (y.compareTo(x) >= 0)
			break
		x = y
	}
	return x
}

fun BigInteger.floorSqrt(): BigInteger {
	astGreaterEqual(this, BIG0)
	if (this <= BIG1)
		return this
	
	var div = BigInteger.ZERO.setBit(this.bitLength() / 2)
	var div2 = div
	// Loop until we hit the same value twice in a row, or wind
	// up alternating.
	var y: BigInteger
	while (true) {
		y = div.add(this.divide(div)).shiftRight(1)
		if (y.equals(div) || y.equals(div2))
			break
		div2 = div
		div = y
	}
	while (y * y < this)
		y += 1
	while (y * y > this)
		y -= 1
	return y
}

fun BigInteger.isSquare(): Boolean {
	val x = this.floorSqrt()
	return x * x == this
}

fun BigInteger.isCube(): Boolean {
	val x = this.floorOfNthRoot(3)
	return x * x * x == this
}

fun BigInteger.println() {
	println(this.toString())
}

fun BigInteger.lastNDigits(n: Int, radix: Int = 10): BigInteger {
	return this % BIG[radix].pow(n)
}

fun BigInteger.firstNDigits(n: Int, radix: Int = 10): BigInteger {
	val s = this.toString(radix)
	return BigInteger(s.substring(0, n.min(s.length)))
}

fun BigInteger.log(base: Int): Double {
	return this.ln() / base.ln()
}


fun BigInteger.ln(): Double {
	val blex = this.bitLength() - 1022 // any value in 60..1023 is ok
	var n = this
	if (blex > 0)
		n = n.shiftRight(blex);
	val res = Math.log(n.toDouble());
	return if (blex > 0) res + blex * LOG2 else res;
}