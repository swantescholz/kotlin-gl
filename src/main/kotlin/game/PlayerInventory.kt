package game

import util.astGreaterEqual
import util.datastructures.DefaultTreeMap

object PlayerInventory {
	enum class Item(val singular: String, val plural: String) {
		Key("key", "keys"),
		Sword("sword", "swords"),
		Wand("wand", "wands");
		
		fun stringForCount(count: Int): String {
			astGreaterEqual(count, 1)
			if (count == 1)
				return "1 $singular"
			return "$count $plural"
		}
	}
	
	private val items = DefaultTreeMap<Item, Int>({ 0 })
	private val newItemsForNextStep = DefaultTreeMap<Item, Int>({ 0 })
	
	fun reset() {
		items.clear()
		newItemsForNextStep.clear()
	}
	
	fun startNewStep() {
		for (item in Item.values()) {
			items[item] = items[item] + newItemsForNextStep[item]
		}
		newItemsForNextStep.clear()
	}
	
	fun addItem(item: Item) {
		newItemsForNextStep[item] = newItemsForNextStep[item] + 1
	}
	
	fun removeItem(item: Item) {
		astGreaterEqual(items[item], 1)
		items[item] = items[item] - 1
	}
	
	fun hasItem(item: Item): Boolean {
		return items[item] > 0
	}
	
	override fun toString(): String {
		var res = ""
		for ((item, count) in items) {
			if (count > 0) {
				res += "${item.stringForCount(count)}\n"
			}
		}
		return res
	}
	
}