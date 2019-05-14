package util.extensions.sequences

import util.astTrue

class AppendSequence<T>(private val startSequence: Sequence<T>,
                        private val appendedSequence: Sequence<T>) : Sequence<T> {
	private class ConcatIterator<T>(private val sequence1: Sequence<T>,
	                                private val sequence2: Sequence<T>) : Iterator<T> {
		
		private val sequence2iterator = sequence2.iterator()
		private var currentIterator = sequence1.iterator()
		private var onSecondSequence = false
		
		override fun hasNext(): Boolean {
			return currentIterator.hasNext() || (currentIterator != sequence2iterator && sequence2iterator.hasNext())
		}
		
		override fun next(): T {
			if (!currentIterator.hasNext()) {
				if (!onSecondSequence) {
					currentIterator = sequence2iterator
					onSecondSequence = true
				}
			}
			astTrue(currentIterator.hasNext())
			return currentIterator.next()
		}
		
	}
	
	override fun iterator(): Iterator<T> {
		return ConcatIterator(startSequence, appendedSequence)
	}
	
}