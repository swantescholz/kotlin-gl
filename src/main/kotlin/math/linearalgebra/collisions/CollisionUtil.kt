package math.linearalgebra.collisions

import math.linearalgebra.Vector2

object CollisionUtil {
	
	private val LINE_MIN_FACTOR = 0.0
	private val LINE_MAX_FACTOR = 1.0
	
	fun intersectLines(startA: Vector2, endA: Vector2, startB: Vector2, endB: Vector2): Vector2? {
		val dira = endA.minus(startA)
		val dirb = endB.minus(startB)
		if (dira.isParallelTo(dirb)) {
			return null
		}
		val num = dira.x * (startB.y - startA.y) + dira.y * (startA.x - startB.x)
		val den = dira.y * dirb.x - dira.x * dirb.y
		val t = num / den
		val s: Double
		if (dira.x == 0.0) {
			s = (startB.y - startA.y + t * dirb.y) / dira.y
		} else {
			s = (startB.x - startA.x + t * dirb.x) / dira.x
		}
		
		if (factorOutsideLineBounds(t) || factorOutsideLineBounds(s)) {
			return null
		}
		return dirb.times(t).plus(startB)
	}
	
	fun getClosestPointOnOtherLine(lineStart: Vector2, lineEnd: Vector2, otherLineStart: Vector2, otherLineEnd: Vector2): Vector2? {
		val intersection = intersectLines(lineStart, lineEnd, otherLineStart, otherLineEnd)
		if (intersection != null) {
			return intersection
		}
		if (otherLineStart.distanceToFiniteLine(lineStart, lineEnd) < otherLineEnd.distanceToFiniteLine(lineStart, lineEnd)) {
			return otherLineStart
		}
		return otherLineEnd
	}
	
	fun getFirstLineCircleIntersection(lineStart: Vector2, lineEnd: Vector2, circleCenter: Vector2, radius: Double): Vector2? {
		val d = lineEnd - lineStart
		val f = lineStart - circleCenter
		val a = d.dot(d)
		val b = 2 * f.dot(d)
		val c = f.dot(f) - radius * radius
		
		var discriminant = b * b - 4.0 * a * c
		if (discriminant < 0) {
			return null
		}
		discriminant = Math.sqrt(discriminant)
		val t1 = (-b - discriminant) / (2 * a)
		val t2 = (-b + discriminant) / (2 * a)
		if (t1 >= 0 && t1 <= 1) {
			return lineStart.plus(d.times(t1))
		}
		if (t2 >= 0 && t2 <= 1) {
			return lineStart.plus(d.times(t2))
		}
		return null
	}
	
	private fun factorOutsideLineBounds(factor: Double): Boolean {
		return factor < LINE_MIN_FACTOR || factor > LINE_MAX_FACTOR
	}
	
	private fun getBallOffsetForLine(lineStart: Vector2, lineEnd: Vector2, ballCenter: Vector2, ballRadius: Double): Vector2 {
		val lineNormal = lineEnd.minus(lineStart).rotate90CCW().normalize().times(ballRadius)
		if (lineNormal.dot(ballCenter) > 0) {
			return -lineNormal
		}
		return lineNormal
	}
	
	fun ballIntersectsLine(ballStart: Vector2, ballEnd: Vector2, ballRadius: Double,
	                       lineStart: Vector2, lineEnd: Vector2): Vector2? {
		val offset = getBallOffsetForLine(lineStart, lineEnd, ballStart, ballRadius)
		val startBallShifted = ballStart.plus(offset)
		val endBallShifted = ballEnd.plus(offset)
		val closestPoint = getClosestPointOnOtherLine(startBallShifted, endBallShifted, lineStart, lineEnd)
		if (closestPoint == null) {
			return null
		} else if (closestPoint === lineStart) {
			return getFirstLineCircleIntersection(ballStart, ballEnd, lineStart, ballRadius)
		} else if (closestPoint === lineEnd) {
			return getFirstLineCircleIntersection(ballStart, ballEnd, lineEnd, ballRadius)
		}
		return closestPoint.minus(offset)
	}
	
	fun isPointInTriangle(p: Vector2, a: Vector2, b: Vector2, c: Vector2): Boolean {
		if (p.minus(a).isRightOf(b.minus(a)))
			return false
		if (p.minus(b).isRightOf(c.minus(b)))
			return false
		return !p.minus(c).isRightOf(a.minus(c))
	}
}
