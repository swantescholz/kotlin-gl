package util.datastructures

import java.util.*

fun <T> treeSetOf(vararg elements: T): TreeSet<T> = elements.toCollection(TreeSet())
fun <K, V> treeMapOf(vararg pairs: Pair<K, V>): TreeMap<K, V>
		= TreeMap<K, V>().apply { putAll(pairs) }

