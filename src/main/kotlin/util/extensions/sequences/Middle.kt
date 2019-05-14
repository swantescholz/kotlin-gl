package util.extensions.sequences

import util.astTrue
import java.util.*


fun <T> Sequence<T>.concat(you: Sequence<T>): Sequence<T> {
	return AppendSequence<T>(this, you)
}

fun <T> Sequence<T>.append(vararg elements: T): Sequence<T> = this.concat(elements.asSequence())
fun <T> Sequence<T>.prepend(vararg elements: T): Sequence<T> = elements.asSequence().concat(this)

fun <T> Sequence<T?>.takeUntilNull(): Sequence<T> {
	return takeWhile { it != null }.map { it!! }
}


inline fun <A, B> Sequence<A>.foldMap(initial: B, crossinline operation: (B, A) -> B): Sequence<B> {
	var current = initial
	return this.map {
		current = operation(current, it)
		return@map current
	}
}

inline fun <T> Sequence<T>.peek(crossinline f: (T) -> Unit) = this.map { f(it); it }


fun <T> Sequence<T>.withIndex1(): Sequence<IndexedValue<T>> {
	return this.withIndex().map { IndexedValue(it.index + 1, it.value) }
}


inline fun <A, B : Comparable<B>> Sequence<A>.takeNSmallestBy(n: Int, transform: (A) -> B): Sequence<A> {
	val maxpq = PriorityQueue<Pair<A, B>>(n, { a: Pair<A, B>?, b: Pair<A, B>? ->
		var res = 0
		if (a != null && b != null) {
			res = -a.second.compareTo(b.second)
		}
		res
	})
	val iter = this.iterator()
	(1..n).forEach {
		if (!iter.hasNext())
			throw IllegalArgumentException("not $n elements in sequence")
		val x = iter.next()
		val elem = Pair(x, transform(x))
		maxpq.offer(elem)
	}
	while (iter.hasNext()) {
		val x = iter.next()
		val y = transform(x)
		if (maxpq.peek().second.compareTo(y) > 0) {
			maxpq.remove()
			maxpq.offer(Pair(x, y))
		}
	}
	val ll = LinkedList<A>()
	while (!maxpq.isEmpty()) {
		ll.addFirst(maxpq.remove().first)
	}
	return ll.asSequence()
}

//drops all elements that are not uniquely only once in the sequence
fun <T> Sequence<T>.filterUnique(): Sequence<T> {
	return this.groupBy { it }.asSequence().filter { it.value.size == 1 }.map { it.key }
}

fun <T> Sequence<T>.intertwine(you: Sequence<T>): Sequence<T> {
	return this.zip(you, { a, b -> sequenceOf(a, b) }).flatten()
}

fun <T, K, V> Sequence<T>.groupByWith(entryMapping: (T) -> Pair<K, V>): Map<K, List<V>> {
	return this.groupByToWith(entryMapping, LinkedHashMap<K, MutableList<V>>())
}

fun <T, K, V> Sequence<T>.groupByToWith(entryMapping: (T) -> Pair<K, V>,
                                        destinationMap: MutableMap<K, MutableList<V>>): Map<K, MutableList<V>> {
	this.forEach { element ->
		val entry = entryMapping(element)
		if (entry.first !in destinationMap)
			destinationMap.put(entry.first, ArrayList<V>())
		destinationMap[entry.first]?.add(entry.second)
	}
	return destinationMap
}

fun <T> Sequence<T>.cutIntoSegments(segmentLength: Int): Sequence<ArrayList<T>> {
	astTrue(segmentLength > 0)
	var list = ArrayList<T>()
	return this.map {
		list.add(it)
		if (list.size == segmentLength) {
			val res = list
			list = ArrayList<T>()
			return@map res
		}
		return@map null
	}.filterNotNull()
}


