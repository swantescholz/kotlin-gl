package util.extensions

import java.util.*

fun <T> ArrayList<T>.swap(index1: Int, index2: Int) {
	val tmp = this[index1]
	this[index1] = this[index2]
	this[index2] = tmp
}

fun <T> ArrayList<T>.removeFirst(): T {
	val res = this.first()
	this.removeAt(0)
	return res
}
fun <T> ArrayList<T>.removeLast(): T {
	val res = this.last()
	this.removeAt(this.size - 1)
	return res
}

fun <T> Array<T>.toArrayList(): ArrayList<T> = toCollection(ArrayList())
fun <T> Sequence<T>.toArrayList(): ArrayList<T> = toCollection(ArrayList())
fun <T> Iterable<T>.toArrayList(): ArrayList<T> = toCollection(ArrayList())
fun <T> Array<T>.toal(): ArrayList<T> = toCollection(ArrayList())
fun <T> Sequence<T>.toal(): ArrayList<T> = toCollection(ArrayList())
fun <T> Iterable<T>.toal(): ArrayList<T> = toCollection(ArrayList())

fun ArrayList<Double>.variance(): Double {
	val mean = this.average()
	return this.asSequence().map { (it - mean) * (it - mean) }.sum() / this.size
}