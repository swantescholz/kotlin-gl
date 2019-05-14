package util.datastructures

import java.util.*

class DefaultHashMap<K, V>(private val defaultGenerator: (K) -> V,
                           private val addWhenQueried: Boolean = true) : HashMap<K, V>() {
	constructor(you: DefaultHashMap<K, V>) : this(you.defaultGenerator, you.addWhenQueried) {
		for (entry in you) {
			put(entry.key, entry.value)
		}
	}
	
	override operator fun get(key: K): V {
		if (key in this)
			return super.get(key)!!
		val value = defaultGenerator(key)
		if (addWhenQueried)
			put(key, value)
		return value
	}
}