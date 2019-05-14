package util.extensions

infix operator fun Int.times(stringToRepeat: String) = stringToRepeat.repeat(this)
infix operator fun Long.times(stringToRepeat: String) = stringToRepeat.repeat(this.toInt())
infix operator fun String.times(numberOfRepetitions: Int) = repeat(numberOfRepetitions)

fun String.asciiSum(): Int {
	return this.map { it.toInt() }.sum()
}


fun String.trimQuotes(): String {
	if (this.length <= 1)
		return "" + this
	if (this[0] == this[this.length - 1]) {
		if (this[0] == '\'' || this[0] == '\"')
			return this.substring(1, this.length - 1)
	}
	return "" + this
}

fun Char.getLetterValue(): Int {
	val ich = this.toInt()
	if (ich >= 65 && ich <= 90) return ich - 64
	if (ich >= 97 && ich <= 122) return ich - 96
	throw IllegalArgumentException("bad letter value")
}

fun String.getWordValue(): Long {
	var sum = 0L
	for (c in this) {
		sum += c.getLetterValue()
	}
	return sum
}

fun String.isPalindrome(): Boolean {
	return this == this.reversed()
}

fun Char.getDigitValue(): Int {
	val ich = this.toByte()
	if (ich >= 48 && ich <= 57) return ich - 48
	return 0
}

fun String.getDigitSum(): Int {
	return this.map { it.getDigitValue() }.sum()
}
