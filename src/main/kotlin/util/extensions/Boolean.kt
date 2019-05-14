package util.extensions

fun Boolean.toInt(): Int {
	if (this)
		return 1
	return 0
}

fun Boolean.toLong(): Long {
	if (this)
		return 1L
	return 0L
}