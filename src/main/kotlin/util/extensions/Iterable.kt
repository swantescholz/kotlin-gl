package util.extensions

import util.astTrue
import java.util.*

fun <T : Any> Iterable<T>.repeat(nTimes: Int = Int.MAX_VALUE): Sequence<T> {
	var it = iterator()
	astTrue(it.hasNext())
	if (nTimes <= 0)
		return emptySequence()
	var n = 0
	return generateSequence {
		if (!it.hasNext()) {
			it = iterator()
			n++
		}
		if (n == nTimes)
			return@generateSequence null
		return@generateSequence it.next()
	}
}

fun <T> Iterable<T>.toTreeSet(): TreeSet<T> {
	return toCollection(TreeSet<T>())
}

fun <K, V> Iterable<Pair<K, V>>.toHashMap(): HashMap<K, V> {
	val hm = HashMap<K, V>()
	hm.putAll(this)
	return hm
}

fun <K, V> Iterable<Pair<K, V>>.toTreeMap(): TreeMap<K, V> {
	val map = TreeMap<K, V>()
	map.putAll(this)
	return map
}

fun <T> Iterable<T>.printlnAll() {
	for (it in this)
		println(it)
}

fun <T> Iterable<T>.withIndex1(): Iterable<IndexedValue<T>> {
	return this.withIndex().map { IndexedValue(it.index + 1, it.value) }
}

fun Iterable<Long>.product(): Long {
	var res = 1L
	for (it in this)
		res *= it
	return res
}

fun Iterable<Int>.lproduct(): Long {
	var res = 1L
	for (it in this)
		res *= it
	return res
}

fun Iterable<Int>.lsum(): Long {
	var res = 0L
	for (it in this)
		res += it
	return res
}

fun <T : Comparable<T>> Iterable<T>.isDescending(): Boolean {
	val iter = this.iterator()
	if (!iter.hasNext())
		return true
	var last = iter.next()
	while (iter.hasNext()) {
		val next = iter.next()
		if (next.compareTo(last) > 0)
			return false
		last = next
	}
	return true
}

fun <T> Iterable<T>.containsOrdered(allegedContents: Iterable<T>): Boolean {
	val contentIter = allegedContents.iterator()
	if (!contentIter.hasNext())
		return true
	var currentAllegedContent = contentIter.next()
	for (it in this) {
		if (it == currentAllegedContent) {
			if (!contentIter.hasNext())
				return true
			currentAllegedContent = contentIter.next()
		}
	}
	return false
}

//iterable should be sorted
fun Iterable<Int>.countConsecutiveInts(start: Int = 1): Int {
	val iter = this.iterator()
	do {
		if (iter.hasNext() == false)
			return 0
	} while (iter.next() != start)
	var count = 1
	while (iter.hasNext() && iter.next() == start + count) {
		count++
	}
	return count
}