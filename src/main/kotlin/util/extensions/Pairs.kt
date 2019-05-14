package util.extensions

import util.extensions.sequences.seq
import util.extensions.sequences.takeUntilNull


fun <A, B> Sequence<A>.pairsWith(you: Sequence<B>): Sequence<Pair<A, B>> {
	return this.map { Pair(it, you) }.flatMap { pa -> you.map { Pair(pa.first, it) } }
}

fun <A, B> Iterable<A>.pairsWith(you: Iterable<B>): Sequence<Pair<A, B>> {
	return this.asSequence().pairsWith(you.asSequence())
}

fun <A> Iterable<A>.pairsWithSelf(): Sequence<Pair<A, A>> {
	return this.pairsWith(this)
}

// [1,2,3,4] -> [(1,2),(1,3),(1,4),(2,3),(2,4),(3,4)]
fun <E> List<E>.seqPairsAscending(): Sequence<Pair<E, E>> {
	val iter1 = iterator()
	if (iter1.hasNext() == false)
		return emptySequence()
	var iter2 = iterator()
	iter2.next()
	var e1 = iter1.next()
	var index1 = 0
	return 1.seq(this.size * (this.size - 1) / 2).map {
		if (iter2.hasNext()) {
			return@map Pair(e1, iter2.next())
		}
		index1++
		if (index1 >= this.size - 1)
			return@map null
		e1 = iter1.next()
		iter2 = iterator()
		for (i in 0..index1)
			iter2.next()
		return@map Pair(e1, iter2.next())
	}.takeUntilNull()
}