package util.string

import java.util.*

val ALPHABET_SMALL = "abcdefghijklmnopqrstuvwxyz".toHashSet()
val ALPHABET_BIG = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toHashSet()
val ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toHashSet()
val READABLE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-=_+[{}]'| \n\t\"\\,./;?<>`~".toHashSet()
val LETTER_FREQUENCIES = hashMapOf(Pair('a', 0.08167), Pair('b', 0.01492), Pair('c', 0.02782), Pair('d', 0.04253), Pair('e', 0.12702), Pair('f', 0.02228), Pair('g', 0.02015), Pair('h', 0.06094), Pair('i', 0.06966), Pair('j', 0.00153), Pair('k', 0.00772), Pair('l', 0.04025), Pair('m', 0.02406), Pair('n', 0.06749), Pair('o', 0.07507), Pair('p', 0.01929), Pair('q', 0.00095), Pair('r', 0.05987), Pair('s', 0.06327), Pair('t', 0.09056), Pair('u', 0.02758), Pair('v', 0.00978), Pair('w', 0.02361), Pair('x', 0.00150), Pair('y', 0.01974), Pair('z', 0.00074))


fun print(vararg args: Any) {
	var s = ""
	for (it in args) {
		s += it.toString() + " "
	}
	System.out.print(s.removeSuffix(" "))
}

fun println(vararg args: Any) {
	var s = ""
	for (it in args) {
		s += it.toString() + " "
	}
	System.out.println(s.removeSuffix(" "))
}

fun pln(vararg args: Any) {
	var s = ""
	for (it in args) {
		s += it.toString() + " "
	}
	System.out.println(s.removeSuffix(" "))
}

fun readArrayList1(text: String): ArrayList<String> {
	val a = ArrayList<String>()
	for (line in text.split("\n")) {
		a.add(line)
	}
	return a
}

fun readArrayList2(text: String): ArrayList<ArrayList<Int>> {
	val a = ArrayList<ArrayList<Int>>()
	for (line in text.split("\n")) {
		val b = ArrayList<Int>()
		for (item in line.split(" ")) {
			b.add(item.toInt())
		}
		a.add(b)
	}
	return a
}

