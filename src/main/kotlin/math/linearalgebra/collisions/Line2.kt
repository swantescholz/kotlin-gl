package math.linearalgebra.collisions

import math.linearalgebra.Vector2

data class Line2(val start: Vector2, val dir: Vector2) {
	val end: Vector2
		get() = start + dir
	// left normal
	val normal: Vector2
		get() = Vector2(-dir.y, dir.x)
	
	data class CollisionPoint(val pos: Vector2, val s: Double, val t: Double, val isTrueCollision: Boolean)
	companion object {
		val NO_COLLISION = CollisionPoint(Vector2.X, -1.0, -1.0, false)
	}
	
	fun collision(other: Line2): CollisionPoint {
		val denominator = dir.x * other.dir.y - dir.y * other.dir.x
		val diff = start - other.start
		val numerator1 = diff.y * other.dir.x - diff.x * other.dir.y
		val numerator2 = diff.y * dir.x - diff.x * dir.y
		
		if (denominator == 0.0)
			return NO_COLLISION
		
		val s = numerator1 / denominator
		val t = numerator2 / denominator
		
		return CollisionPoint(start + dir * s, s, t, s in 0.0..1.0 && t in 0.0..1.0)
	}
}