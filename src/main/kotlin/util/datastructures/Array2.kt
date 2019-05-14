package util.datastructures

import util.extensions.sequences.seq
import java.util.*

//indexed (x,y)
open class Array2<T>(val width: Int, val height: Int, initFunction: (Int, Int) -> T) {
	
	protected val data = ArrayList<ArrayList<T>>()
	
	init {
		for (x in 0..width - 1) {
			val column = ArrayList<T>()
			for (y in 0..height - 1) {
				column.add(initFunction(x, y))
			}
			data.add(column)
		}
	}
	
	operator fun get(x: Int, y: Int): T {
		return data[x][y]
	}
	
	operator fun get(xyPair: Pair<Int, Int>): T {
		return data[xyPair.first][xyPair.second]
	}
	
	operator fun set(x: Int, y: Int, value: T) {
		data[x][y] = value
	}
	
	operator fun set(xyPair: Pair<Int, Int>, value: T) {
		data[xyPair.first][xyPair.second] = value
	}
	
	fun asSequence(): Sequence<Triple<Int, Int, T>> {
		return 0.seq(height - 1).map { y ->
			0.seq(width - 1).map { x ->
				Triple(x, y, this[x, y])
			}
		}.flatten()
	}
	
	override fun toString(): String {
		var text = ""
		for (y in 0..height - 1) {
			var row = ""
			for (x in 0..width - 1) {
				row += this[x, y].toString() + " "
			}
			text += row.removeSuffix(" ") + "\n"
		}
		return text.removeSuffix("\n")
	}
	
}