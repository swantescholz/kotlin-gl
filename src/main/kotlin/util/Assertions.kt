package util

import math.DEFAULT_EPSILON
import util.extensions.abs
import util.extensions.almostEqual

const val DEFAULT_ERROR_MESSAGE = "<error>"

class MyAssertion(message: String = "assertion failed: $DEFAULT_ERROR_MESSAGE") : RuntimeException(message) {
	init {
		println(message)
	}
}

fun astFail(message: String = "assertion failed: $DEFAULT_ERROR_MESSAGE") {
	throw MyAssertion(message)
}

fun <T> astNull(value: T?, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value != null)
		throw MyAssertion("$message: value $value is not null")
}

fun <T> astNotNull(value: T?, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value == null)
		throw MyAssertion("$message: value $value is null")
}

fun <T> astIn(value: T, collection: Collection<T>, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value !in collection)
		throw MyAssertion("$message: value $value is not in collection")
}

fun astIn(value: Int, range: IntRange, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value !in range)
		throw MyAssertion("$message: value $value is not in range $range")
}

fun astIn(value: Long, range: LongRange, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value !in range)
		throw MyAssertion("$message: value $value is not in range $range")
}

fun astIn(value: Double, range: ClosedRange<Double>, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value !in range)
		throw MyAssertion("$message: value $value is not in range $range")
}

fun <T> astNotIn(value: T, collection: Collection<T>, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value in collection)
		throw MyAssertion("$message: value $value is in collection")
}


fun astTrue(value: Boolean, message: String = DEFAULT_ERROR_MESSAGE) {
	if (!value)
		throw MyAssertion("$message: value is not true")
}

fun astFalse(value: Boolean, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value)
		throw MyAssertion("$message: value is not false")
}

fun astEqual(value1: Int, value2: Long, message: String = DEFAULT_ERROR_MESSAGE) {
	astEqual(value1.toLong(), value2, message)
}

fun astEqual(value1: Long, value2: Int, message: String = DEFAULT_ERROR_MESSAGE) {
	astEqual(value1, value2.toLong(), message)
}

fun <T : Any> astEqual(value1: T, value2: T, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value1.javaClass != value2.javaClass) {
		throw MyAssertion("$value1 and $value2 have different types: ${value1.javaClass} != ${value2.javaClass}")
	}
	if (value1 != value2)
		throw MyAssertion("$message: $value1 != $value2")
}

fun <T : Any> astUnequal(value1: T, value2: T, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value1.javaClass != value2.javaClass)
		throw MyAssertion("$value1 and $value2 have different types: ${value1.javaClass} != ${value2.javaClass}")
	if (value1 == value2)
		throw MyAssertion("$message: $value1 == $value2")
}

fun <T : Comparable<T>> astLess(value1: T, value2: T, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value1 >= value2)
		throw MyAssertion("$message: $value1 >= $value2")
}

fun <T : Comparable<T>> astGreater(value1: T, value2: T, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value1 <= value2)
		throw MyAssertion("$message: $value1 <= $value2")
}

fun <T : Comparable<T>> astLessEqual(value1: T, value2: T, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value1 > value2)
		throw MyAssertion("$message: $value1 > $value2")
}

fun <T : Comparable<T>> astGreaterEqual(value1: T, value2: T, message: String = DEFAULT_ERROR_MESSAGE) {
	if (value1 < value2)
		throw MyAssertion("$message: $value1 < $value2")
}

fun astAlmostEqual(value1: Double, value2: Double, epsilon: Double = DEFAULT_EPSILON, message: String = DEFAULT_ERROR_MESSAGE) {
	if (!value1.almostEqual(value2, epsilon = epsilon))
		throw MyAssertion("$message: |$value1 - $value2| = ${(value2 - value1).abs()} >= eps = $epsilon")
}
