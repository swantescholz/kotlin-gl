package util.datastructures

import java.util.*


// update priority by adding element again (old occurrence is removed automatically)
class DynamicPriorityQueue<K>(val isMaxQueue: Boolean = false) {
	
	private class MyComparator<A>(val queue: DynamicPriorityQueue<A>, isMaxQueue: Boolean) : Comparator<A> {
		val sign = if (isMaxQueue) -1 else 1
		
		override fun compare(o1: A, o2: A): Int {
			if (o1 == o2)
				return 0
			if (queue.priorities[o2]!! - queue.priorities[o1]!! < 0)
				return sign
			return -sign
		}
		
	}
	
	constructor(you: DynamicPriorityQueue<K>) : this(you.isMaxQueue) {
		priorities.putAll(you.priorities)
		treeSet.addAll(you.treeSet)
	}
	
	private val priorities = HashMap<K, Double>()
	private val treeSet = TreeSet<K>(MyComparator(this, isMaxQueue))
	
	val size: Int
		get() = treeSet.size
	
	fun isEmpty() = (size == 0)
	
	operator fun contains(key: K) = key in treeSet
	
	fun add(newElement: K, priority: Double) {
		if (newElement in priorities)
			treeSet.remove(newElement)
		priorities[newElement] = priority
		treeSet.add(newElement)
	}
	
	fun remove(element: K) {
		treeSet.remove(element)
		priorities.remove(element)
	}
	
	fun getPriorityOf(element: K): Double {
		return priorities[element]!!
	}
	
	
	fun first(): K = treeSet.first()
	fun firstWithPriority(): Pair<K, Double> {
		val res = treeSet.first()
		val priority = priorities[res]!!
		return Pair(res, priority)
	}
	
	fun poll(): K {
		val res = treeSet.pollFirst()
		priorities.remove(res)
		return res
	}
	
	fun pollWithPriority(): Pair<K, Double> {
		val res = treeSet.pollFirst()
		val priority = priorities[res]!!
		priorities.remove(res)
		return Pair(res, priority)
	}
	
}
