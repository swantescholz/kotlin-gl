package util.extensions.sequences

import math.LOG2
import util.extensions.big
import util.extensions.max
import util.extensions.min
import util.extensions.println
import java.math.BigInteger
import java.text.DecimalFormat
import java.util.*


fun <T> Sequence<T>.toTreeSet(): TreeSet<T> {
	return toCollection(TreeSet<T>())
}

fun <K, V> Sequence<Pair<K, V>>.toHashMap(): HashMap<K, V> {
	val hm = HashMap<K, V>()
	hm.putAll(this)
	return hm
}

fun <K, V> Sequence<Pair<K, V>>.toSortedMap(comparator: ((K, K) -> Int)? = null): TreeMap<K, V> {
	val res = TreeMap<K, V>(comparator)
	this.forEach { res.put(it.first, it.second) }
	return res
}


fun <T> Sequence<T>.printlnAll() {
	for (it in this)
		println(it)
}

fun <T> Sequence<T>.printlnAllIndexed() {
	this.withIndex().forEach { print(it.index.toString() + " "); println(it.value) }
}

fun Sequence<Int>.minmax(): Pair<Int, Int> {
	var lo = Int.MAX_VALUE
	var hi = Int.MIN_VALUE
	this.forEach {
		lo = lo.min(it)
		hi = hi.max(it)
	}
	return Pair(lo, hi)
}

fun Sequence<Long>.lminmax(): Pair<Long, Long> {
	var lo = Long.MAX_VALUE
	var hi = Long.MIN_VALUE
	this.forEach {
		lo = lo.min(it)
		hi = hi.max(it)
	}
	return Pair(lo, hi)
}


inline fun <T> Sequence<T>.sumByLong(selector: (T) -> Long): Long {
	var sum: Long = 0
	for (element in this) {
		sum += selector(element)
	}
	return sum
}

fun Sequence<BigInteger>.sum() = this.fold(0.big(), { a, b -> a + b })
fun Sequence<BigInteger>.product() = this.fold(1.big(), { a, b -> a * b })
fun Sequence<Int>.product() = this.fold(1, { a, b -> a * b })
fun Sequence<Int>.lproduct() = this.fold(1L, { a, b -> a * b })
fun Sequence<Long>.product() = this.fold(1L, { a, b -> a * b })
fun Sequence<Double>.product() = this.fold(1.0, { a, b -> a * b })

fun Sequence<Number>.printInfo() {
	val seq = this.map { it.toDouble() }.toCollection(arrayListOf<Double>()).asSequence()
	val df = DecimalFormat("#.####")
	fun Sequence<Double>.pln(prefix: String) = this.map {
		if (it.toLong().toDouble() == it)
			return@map it.toLong().toString()
		return@map df.format(it)
	}.joinToString(separator = ", ",
			prefix = prefix.padEnd(15) + ": ").println()
	seq.pln("normal")
	seq.zip(seq.drop(1)).map { it.second - it.first }.pln("differences")
	seq.zip(seq.drop(1)).map { it.second / it.first.toDouble() }.pln("slope")
	seq.map { Math.log(it.toDouble()) }.pln("log_e")
	seq.map { Math.log(it.toDouble()) / LOG2 }.pln("log_2")
	seq.map { Math.sqrt(it.toDouble()) }.pln("sqrts")
	seq.map { it * it }.pln("squares")
	seq.map { it * it * it }.pln("cubes")
}

