package util.datastructures

import java.util.*

class TwoKeyHashMap<A, B, V> {
	private val mapByKeyA = LinkedHashMap<A, LinkedHashMap<B, V>>()
	private val mapByKeyB = LinkedHashMap<B, LinkedHashMap<A, V>>()
	
	operator fun contains(keys: Pair<A, B>) = keys.first in mapByKeyA && keys.second in mapByKeyB
	
	fun clear() {
		mapByKeyA.clear()
		mapByKeyB.clear()
	}
	
	fun removeAllByA(keyA: A) {
		if (keyA in mapByKeyA) {
			for ((keyB, value) in mapByKeyA[keyA]!!)
				remove(keyA, keyB)
		}
	}
	
	fun removeAllByB(keyB: B) {
		if (keyB in mapByKeyB) {
			for ((keyA, value) in mapByKeyB[keyB]!!)
				remove(keyA, keyB)
		}
	}
	
	fun remove(keyA: A, keyB: B) {
		mapByKeyA[keyA]?.remove(keyB)
		mapByKeyB[keyB]?.remove(keyA)
		if (mapByKeyA[keyA]?.isEmpty() ?: false)
			mapByKeyA.remove(keyA)
		if (mapByKeyB[keyB]?.isEmpty() ?: false)
			mapByKeyB.remove(keyB)
	}
	
	operator fun set(keyA: A, keyB: B, value: V) {
		if (keyA !in mapByKeyA) {
			mapByKeyA[keyA] = LinkedHashMap()
		}
		if (keyB !in mapByKeyB) {
			mapByKeyB[keyB] = LinkedHashMap()
		}
		mapByKeyA[keyA]!![keyB] = value
		mapByKeyB[keyB]!![keyA] = value
	}
	
	operator fun get(keyA: A, keyB: B): V = mapByKeyA[keyA]!![keyB]!!
	fun getAllByA(keyA: A): LinkedHashMap<B, V> = mapByKeyA[keyA]!!
	fun getAllByB(keyB: B): LinkedHashMap<A, V> = mapByKeyB[keyB]!!
	
}