package math.linearalgebra.collisions

import util.extensions.abs

data class Coord2i(val x: Int, val y: Int) {
	operator fun plus(you: Coord2i) = Coord2i(x + you.x, y + you.y)
	operator fun minus(you: Coord2i) = Coord2i(x - you.x, y - you.y)
	operator fun times(you: Int) = Coord2i(x * you, y * you)
	
	fun rotate(numClockwiseTurns: Int): Coord2i {
		when (numClockwiseTurns) {
			1 -> return Coord2i(y, -x)
			2 -> return Coord2i(-x, -y)
			3 -> return Coord2i(-y, x)
			else -> return this
		}
	}
	
	val manhatten: Int
		get() = x.abs() + y.abs()
	
	fun isNeighbor4Of(other: Coord2i): Boolean {
		if (x == other.x)
			return (y - other.y).abs() == 1
		if (y == other.y)
			return (x - other.x).abs() == 1
		return false
	}
	
	fun isNeighbor8Of(other: Coord2i): Boolean {
		if (this == other)
			return false
		if ((x - other.x).abs() > 1)
			return false
		if ((y - other.y).abs() > 1)
			return false
		return true
	}
	
	companion object {
		val OFFSETS4 = arrayListOf(Coord2i(1, 0), Coord2i(0, 1), Coord2i(-1, 0), Coord2i(0, -1))
		val OFFSETS8 = arrayListOf(Coord2i(1, 0), Coord2i(1, 1), Coord2i(0, 1), Coord2i(-1, 1),
				Coord2i(-1, 0), Coord2i(-1, -1), Coord2i(0, -1), Coord2i(1, -1))
	}
}