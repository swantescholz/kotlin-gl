package math.linearalgebra.collisions

import util.extensions.max
import util.extensions.min

data class Cuboid(val x: Long, val y: Long, val z: Long, val a: Long, val b: Long, val c: Long) {
	val volume = a * b * c
	
	fun collidesWith(you: Cuboid): Boolean {
		if (x > you.x + you.a || you.x > x + a)
			return false
		if (y > you.y + you.b || you.y > y + b)
			return false
		if (z > you.z + you.c || you.z > z + c)
			return false
		return true
	}
	
	fun intersection(you: Cuboid): Cuboid? {
		if (!collidesWith(you))
			return null
		val (newx1, newx2) = Pair(x.max(you.x), (x + a).min(you.x + you.a))
		val (newy1, newy2) = Pair(y.max(you.y), (y + b).min(you.y + you.b))
		val (newz1, newz2) = Pair(z.max(you.z), (z + c).min(you.z + you.c))
		return Cuboid(newx1, newy1, newz1, newx2 - newx1, newy2 - newy1, newz2 - newz1)
	}
}