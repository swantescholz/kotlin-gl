package math.linearalgebra.collisions

data class Coord2l(val x: Long, val y: Long) {
	operator fun plus(you: Coord2l) = Coord2l(x + you.x, y + you.y)
	operator fun minus(you: Coord2l) = Coord2l(x - you.x, y - you.y)
	operator fun times(you: Long) = Coord2l(x * you, y * you)
	
	fun rotate(numClockwiseTurns: Int): Coord2l {
		when (numClockwiseTurns) {
			1 -> return Coord2l(y, -x)
			2 -> return Coord2l(-x, -y)
			3 -> return Coord2l(-y, x)
			else -> return this
		}
	}
}