package util.extensions

import java.util.*

fun <T> LinkedList<T>.removeIf(filterFunction: (T) -> Boolean) {
	val iter = this.listIterator()
	while (iter.hasNext()) {
		if (filterFunction(iter.next())) {
			iter.remove()
		}
	}
}