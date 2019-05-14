package util.extensions

import java.util.*

fun <A, B> fst(pair: Pair<A, B>): A = pair.first
fun <A, B> snd(pair: Pair<A, B>): B = pair.second
//fun <A,B,C> fst(triple: Triple<A,B,C>) : A = triple.first
//fun <A,B,C> snd(triple: Triple<A,B,C>) : B = triple.second
//fun <A,B,C> trd(triple: Triple<A,B,C>) : C = triple.third

fun <A, T> Pair<A, A>.map(function: (A) -> T) = Pair(function(first), function(second))
operator fun <A : Comparable<A>, B : Comparable<B>> Pair<A, B>.compareTo(other: Pair<A, B>): Int {
	first.compareTo(other.first).let {
		if (it != 0)
			return it
	}
	return second.compareTo(other.second)
}

operator fun <A : Comparable<A>, B : Comparable<B>, C : Comparable<C>> Triple<A, B, C>.compareTo(other: Triple<A, B, C>): Int {
	first.compareTo(other.first).let {
		if (it != 0)
			return it
	}
	second.compareTo(other.second).let {
		if (it != 0)
			return it
	}
	return third.compareTo(other.third)
}

fun <A : Comparable<A>> Pair<A, A>.sorted(): Pair<A, A> {
	if (first <= second)
		return this
	return Pair(second, first)
}

val <A, B> Pair<A, B>.a: A
	get() = this.first
val <A, B> Pair<A, B>.b: B
	get() = this.second
val <A, B, C> Triple<A, B, C>.a: A
	get() = this.first
val <A, B, C> Triple<A, B, C>.b: B
	get() = this.second
val <A, B, C> Triple<A, B, C>.c: C
	get() = this.third

data class MutablePair<A, B>(var first: A, var second: B) {}
data class MutableTriple<A, B, C>(var first: A, var second: B, var third: C) {}

fun Triple<Long, Long, Long>.sum(): Long {
	return this.first + this.second + this.third
}

fun Triple<Long, Long, Long>.product(): Long {
	return this.first * this.second * this.third
}

class PriorityPair<T>(val priority: Double, val value: T) : Comparable<PriorityPair<T>> {
	override fun compareTo(other: PriorityPair<T>): Int {
		return (this.priority - other.priority).sign()
	}
	
	operator fun component1(): Double = priority
	operator fun component2(): T = value
}

data class Complexl(val a: Long, val b: Long) : Comparable<Complexl> {
	override fun compareTo(other: Complexl): Int {
		if (a < other.a)
			return -1
		if (a > other.a)
			return 1
		if (b < other.b)
			return -1
		if (b > other.b)
			return 1
		return 0
	}
	
	fun sorted() = if (a < b) this else Complexl(b, a)
	
}

data class Tuple3l(val a: Long, val b: Long, val c: Long) : Comparable<Tuple3l> {
	constructor(a: Int, b: Int, c: Int) : this(a.toLong(), b.toLong(), c.toLong())
	
	override fun compareTo(other: Tuple3l): Int {
		if (a < other.a)
			return -1
		if (a > other.a)
			return 1
		if (b < other.b)
			return -1
		if (b > other.b)
			return 1
		if (c < other.c)
			return -1
		if (c > other.c)
			return 1
		return 0
	}
}

data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

operator fun <A : Comparable<A>, B : Comparable<B>, C : Comparable<C>,
		D : Comparable<D>> Tuple4<A, B, C, D>.compareTo(other: Tuple4<A, B, C, D>): Int {
	if (a < other.a)
		return -1
	if (a > other.a)
		return 1
	if (b < other.b)
		return -1
	if (b > other.b)
		return 1
	if (c < other.c)
		return -1
	if (c > other.c)
		return 1
	return 0
}

data class Tuple4l(val a: Long, val b: Long, val c: Long, val d: Long) : Comparable<Tuple4l> {
	override operator fun compareTo(other: Tuple4l): Int {
		if (a < other.a)
			return -1
		if (a > other.a)
			return 1
		if (b < other.b)
			return -1
		if (b > other.b)
			return 1
		if (c < other.c)
			return -1
		if (c > other.c)
			return 1
		if (d < other.d)
			return -1
		if (d > other.d)
			return 1
		return 0
	}
	
}

data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)
data class Tuple5l(val a: Long, val b: Long, val c: Long, val d: Long, val e: Long) : Comparable<Tuple5l> {
	override operator fun compareTo(other: Tuple5l): Int {
		if (a < other.a)
			return -1
		if (a > other.a)
			return 1
		if (b < other.b)
			return -1
		if (b > other.b)
			return 1
		if (c < other.c)
			return -1
		if (c > other.c)
			return 1
		if (d < other.d)
			return -1
		if (d > other.d)
			return 1
		if (e < other.e)
			return -1
		if (e > other.e)
			return 1
		return 0
	}
}

data class Tuple6<A, B, C, D, E, F>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F)
data class Tuple6l(val a: Long, val b: Long, val c: Long, val d: Long, val e: Long, val f: Long)

class Tuple<T>(vararg args: T) : ArrayList<T>(args.toCollection(arrayListOf<T>())) {
	val a: T get() = this[0]!!
	val b: T get() = this[1]!!
	val c: T get() = this[2]!!
	val d: T get() = this[3]!!
	val e: T get() = this[4]!!
	val f: T get() = this[5]!!
	val g: T get() = this[6]!!
	val h: T get() = this[7]!!
	val i: T get() = this[8]!!
	val j: T get() = this[9]!!
	val k: T get() = this[10]!!
	val l: T get() = this[11]!!
	val m: T get() = this[12]!!
	val n: T get() = this[13]!!
	val o: T get() = this[14]!!
	val p: T get() = this[15]!!
	val q: T get() = this[16]!!
	val r: T get() = this[17]!!
	val s: T get() = this[18]!!
	val t: T get() = this[19]!!
	
	operator fun component1() = this[0]!!
	operator fun component2() = this[1]!!
	operator fun component3() = this[2]!!
	operator fun component4() = this[3]!!
	operator fun component5() = this[4]!!
	operator fun component6() = this[5]!!
	operator fun component7() = this[6]!!
	operator fun component8() = this[7]!!
	operator fun component9() = this[8]!!
	operator fun component10() = this[9]!!
	operator fun component11() = this[10]!!
	operator fun component12() = this[11]!!
	operator fun component13() = this[12]!!
	operator fun component14() = this[13]!!
	operator fun component15() = this[14]!!
	operator fun component16() = this[15]!!
	operator fun component17() = this[16]!!
	operator fun component18() = this[17]!!
	operator fun component19() = this[18]!!
	operator fun component20() = this[19]!!
	
}