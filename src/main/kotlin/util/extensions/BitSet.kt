package util.extensions

import java.util.*

infix operator fun BitSet.contains(n: Int): Boolean = this.get(n)

operator fun BitSet.iterator(): Iterator<Int> {
	class MyIterator(val bitSet: BitSet) : Iterator<Int> {
		var currentIndex = bitSet.nextSetBit(0)
		override fun next(): Int {
			val res = currentIndex
			if (currentIndex == Integer.MAX_VALUE)
				currentIndex = -1
			else
				currentIndex = bitSet.nextSetBit(currentIndex + 1)
			return res
		}
		
		override fun hasNext() = currentIndex >= 0
	}
	return MyIterator(this)
}

fun BitSet.asSequence(): Sequence<Int> = this.iterator().asSequence()
fun BitSet.ass() = asSequence()