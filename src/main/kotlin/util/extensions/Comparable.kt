package util.extensions

fun <T : Comparable<T>> T.min(you: T): T {
	if (this.compareTo(you) <= 0)
		return this
	return you
}

fun <T : Comparable<T>> T.max(you: T): T {
	if (this.compareTo(you) > 0)
		return this
	return you
}