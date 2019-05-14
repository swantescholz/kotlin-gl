package util.extensions

import util.range
import java.util.*

fun <T> List<T>.get(index: Long) = this.get(index.toInt())

fun <E> MutableList<E>.swapElements(index1: Int, index2: Int) {
	val tmp = this.get(index1)
	this[index1] = this[index2]
	this[index2] = tmp
}

fun <T : Comparable<T>> List<List<T>>.sortedByInnerLists(): List<List<T>> {
	class MyComparator : Comparator<List<T>> {
		override fun compare(list1: List<T>, list2: List<T>): Int {
			for (i in range(list1.size.min(list2.size))) {
				if (list1[i] < list2[i])
					return -1
				if (list1[i] > list2[i])
					return 1
			}
			if (list1.size < list2.size)
				return -1
			if (list1.size > list2.size)
				return 1
			return 0
		}
	}
	return this.sortedWith(MyComparator())
}

fun <E : Comparable<E>> List<E>.isAscending(): Boolean {
	var last: E? = null
	for (e in this) {
		if (last != null && last > e)
			return false
		last = e
	}
	return true
}

fun <E : Comparable<E>> List<E>.isDescending(): Boolean {
	var last: E? = null
	for (e in this) {
		if (last != null && last < e)
			return false
		last = e
	}
	return true
}