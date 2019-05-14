package math.linearalgebra

import util.extensions.almostEqual
import java.util.*

class Vector2(val x: Double = 0.0, val y: Double = 0.0) : VectorSpace<Vector2>() {
	
	constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())
	
	constructor(x: Long, y: Long) : this(x.toDouble(), y.toDouble())
	
	fun data(): DoubleArray {
		return doubleArrayOf(x, y)
	}
	
	fun rotate(radCCW: Double): Vector2 {
		val cs = Math.cos(radCCW)
		val sn = Math.sin(radCCW)
		return Vector2(x * cs - y * sn, x * sn + y * cs)
	}
	
	fun rotate90CCW() = Vector2(-y, x)
	fun rotate90CW() = Vector2(y, -x)
	
	fun length(): Double {
		return Math.sqrt(x * x + y * y)
	}
	
	fun length2(): Double {
		return x * x + y * y
	}
	
	fun dot(you: Vector2): Double {
		return x * you.x + y * you.y
	}
	
	fun distanceTo(you: Vector2): Double {
		return Math.sqrt((x - you.x) * (x - you.x) + (y - you.y) * (y - you.y))
	}
	
	fun distanceTo2(you: Vector2): Double {
		return (x - you.x) * (x - you.x) + (y - you.y) * (y - you.y)
	}
	
	override operator fun unaryMinus(): Vector2 {
		return Vector2(-x, -y)
	}
	
	override operator fun plus(o: Vector2): Vector2 {
		return Vector2(x + o.x, y + o.y)
	}
	
	override operator fun minus(o: Vector2): Vector2 {
		return Vector2(x - o.x, y - o.y)
	}
	
	override operator fun times(o: Double): Vector2 {
		return Vector2(x * o, y * o)
	}
	
	override fun almostEqual(o: Vector2): Boolean {
		return x.almostEqual(o.x) && y.almostEqual(o.y)
	}
	
	
	fun normalize(): Vector2 {
		val lsq = length2()
		if (lsq.almostEqual(1.0))
			return this
		return this / Math.sqrt(lsq)
	}
	
	fun angle(you: Vector2): Double {
		return Math.acos(dot(you) / Math.sqrt(this.length2() * you.length2()))
	}
	
	// this vector is the mirror line
	fun mirrorPoint(point: Vector2): Vector2 {
		val n = this.normalize()
		return 2.0 * n * (n.dot(point)) - point
	}
	
	fun isLeftOf(other: Vector2) = x * other.y < other.x * y
	fun isRightOf(other: Vector2) = x * other.y > other.x * y
	
	override fun toString(): String {
		return "($x, $y)"
	}
	
	companion object {
		val X = Vector2(1.0, 0.0)
		val Y = Vector2(0.0, 1.0)
		val XY = Vector2(1.0, 1.0)
		val ZERO = Vector2(0.0, 0.0)
		val ONE = Vector2(1.0, 1.0)
	}
	
	operator fun component1(): Double = x
	operator fun component2(): Double = y
	
	fun toArrayList(): ArrayList<Double> {
		return arrayListOf(x, y)
	}
	
	fun cross(other: Vector2): Double {
		return x * other.y - y * other.x
	}
	
	fun isParallelTo(other: Vector2): Boolean {
		return cross(other) == 0.0
	}
	
	fun negateY(): Vector2 {
		return Vector2(x, -y)
	}
	
	fun setLength(newLength: Double): Vector2 {
		return normalize() * newLength
	}
	
	fun distanceToInfiniteLine(lineStart: Vector2, lineEnd: Vector2): Double {
		val normal = lineEnd.minus(lineStart).rotate90CCW().normalize()
		return Math.abs(normal.dot(-lineStart))
	}
	
	fun distanceToFiniteLine(lineStart: Vector2, lineEnd: Vector2): Double {
		val d = lineEnd - lineStart
		if (d.dot(-lineStart) < 0) {
			return distanceTo(lineStart)
		}
		if ((-d).dot(-lineEnd) < 0) {
			return distanceTo(lineEnd)
		}
		return distanceToInfiniteLine(lineStart, lineEnd)
	}
	
	@JvmOverloads fun to3(z: Double = 0.0): Vector3 {
		return Vector3(x, y, z)
	}
	
}

