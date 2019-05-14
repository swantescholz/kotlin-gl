package gl.util

import java.util.*

class IndexPool {
	
	private val free = LinkedList<Int>()
	private val used = LinkedList<Int>()
	
	fun add(value: Int) {
		free.add(value)
	}
	
	fun acquire(): Int {
		val value = free.poll() ?: throw IllegalStateException("No free index left to acquire.")
		used.add(value)
		return value
	}
	
	fun release(value: Int) {
		if (used.contains(value)) {
			used.remove(value)
			free.add(value)
		}
	}
	
}
