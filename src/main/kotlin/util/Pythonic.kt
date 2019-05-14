package util

fun pass() {
}

fun range(size: Long): LongRange {
	return 0L..(size - 1)
}

fun range(size: Int): IntRange {
	return 0..(size - 1)
}

fun <T> range(collection: Collection<T>): IntRange {
	return 0..(collection.size - 1)
}

fun <T> range(array: Array<T>): IntRange {
	return 0..(array.size - 1)
}