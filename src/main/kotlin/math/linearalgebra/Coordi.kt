package math.linearalgebra

import util.extensions.max
import util.extensions.min

class Coordi(val x: Int, val y: Int) : VectorSpacei<Coordi>(), Comparable<Coordi> {
	override fun compareTo(other: Coordi): Int {
		if (y < other.y)
			return -1
		if (y > other.y)
			return 1
		return x.compareTo(other.x)
	}
	
	override fun unaryMinus(): Coordi = Coordi(-x, -y)
	
	override fun plus(o: Coordi): Coordi = Coordi(x + o.x, y + o.y)
	
	override fun minus(o: Coordi): Coordi = Coordi(x - o.x, y - o.y)
	
	override fun times(o: Int): Coordi = Coordi(x * o, y * o)
	operator fun times(o: Coordi): Coordi = Coordi(x * o.x, y * o.y)
	
	fun min(o: Coordi) = Coordi(x.min(o.x), y.min(o.y))
	fun max(o: Coordi) = Coordi(x.max(o.x), y.max(o.y))
	
	val vec2: Vector2
		get() = Vector2(x, y)
	
	override fun toString() = "($x, $y)"
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false
		other as Coordi
		if (x != other.x) return false
		if (y != other.y) return false
		return true
	}
	
	override fun hashCode(): Int {
		var result = x
		result = 31 * result + y
		return result
	}
	
	operator fun component1(): Int = x
	operator fun component2(): Int = y
}