package util.extensions

import math.DEFAULT_EPSILON
import math.DEGREE_TO_RADIAN_FACTOR
import math.RADIAN_TO_DEGREE_FACTOR


fun Double.toRadian() = DEGREE_TO_RADIAN_FACTOR * this
fun Double.toDegree() = RADIAN_TO_DEGREE_FACTOR * this
fun Double.square(): Double = this * this

fun Double.log(base: Int) = this.ln() / base.ln()
fun Double.ln() = Math.log(this)
fun Double.exp() = Math.exp(this)

fun Double.sin() = Math.sin(this)
fun Double.cos() = Math.cos(this)
fun Double.tan() = Math.tan(this)
fun Double.csc() = 1.0 / Math.sin(this)
fun Double.asin() = Math.asin(this)
fun Double.acos() = Math.acos(this)
fun Double.atan() = Math.atan(this)
fun Double.acsc() = Math.asin(1.0 / this)

fun Double.cube() = this * this * this
fun Double.cbrt() = Math.cbrt(this)

fun Double.toComparableInt(): Int {
	if (this > 0.0)
		return 1
	if (this < 0.0)
		return -1
	return 0
}

fun Double.almostEqual(you: Double, epsilon: Double = DEFAULT_EPSILON): Boolean {
	return this.diff(you) < epsilon
}

fun Double.floor(): Double {
	return Math.floor(this)
}

fun Double.ceil(): Double {
	return Math.ceil(this)
}

fun Double.sign(): Int {
	if (this > 0.0)
		return 1
	if (this < 0.0)
		return -1
	return 0
}

fun Double.pow(exponent: Number): Double {
	return Math.pow(this, exponent.toDouble())
}

infix fun Double.diff(you: Double): Double {
	return Math.abs(this - you)
}

fun Double.min(you: Double): Double {
	return Math.min(this, you)
}

fun Double.max(you: Double): Double {
	return Math.max(this, you)
}

fun Double.clamp(min: Double, max: Double): Double = this.max(min).min(max)

fun Double.abs(): Double {
	return Math.abs(this)
}

fun Double.sqrt(): Double {
	return Math.sqrt(this)
}

fun Double.nearlyInt(delta: Double = DEFAULT_EPSILON): Boolean {
	return Math.abs(this - Math.round(this)) < delta
}

fun Double.round(): Long {
	return Math.round(this)
}