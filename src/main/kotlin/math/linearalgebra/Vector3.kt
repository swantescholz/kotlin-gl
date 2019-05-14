package math.linearalgebra

import util.extensions.almostEqual

class Vector3(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0) : VectorSpace<Vector3>() {
	
	
	constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble()) {
		
	}
	
	constructor(vector2: Vector2, z: Double = 0.0) : this(vector2.x, vector2.y, z)
	
	operator fun component1(): Double = x
	operator fun component2(): Double = y
	operator fun component3(): Double = z
	
	fun length(): Double {
		return Math.sqrt(x * x + y * y + z * z)
	}
	
	fun length2(): Double {
		return x * x + y * y + z * z
	}
	
	fun dot(you: Vector3): Double {
		return x * you.x + y * you.y + z * you.z
	}
	
	fun distanceTo(you: Vector3): Double {
		return Math.sqrt((x - you.x) * (x - you.x) + (y - you.y) * (y - you.y) + (z - you.z) * (z - you.z))
	}
	
	fun distanceTo2(you: Vector3): Double {
		return (x - you.x) * (x - you.x) + (y - you.y) * (y - you.y) + (z - you.z) * (z - you.z)
	}
	
	override operator fun unaryMinus(): Vector3 {
		return Vector3(-x, -y, -z)
	}
	
	override operator fun plus(o: Vector3): Vector3 {
		return Vector3(x + o.x, y + o.y, z + o.z)
	}
	
	override operator fun minus(o: Vector3): Vector3 {
		return Vector3(x - o.x, y - o.y, z - o.z)
	}
	
	override operator fun times(o: Double): Vector3 {
		return Vector3(x * o, y * o, z * o)
	}
	
	override fun almostEqual(o: Vector3): Boolean {
		return x.almostEqual(o.x) && y.almostEqual(o.y) && z.almostEqual(o.z)
	}
	
	
	fun normalize(): Vector3 {
		val lsq = length2()
		if (lsq.almostEqual(1.0))
			return this
		return this / Math.sqrt(lsq)
	}
	
	fun angle(you: Vector3): Double {
		return Math.acos(dot(you) / Math.sqrt(this.length2() * you.length2()))
	}
	
	fun cross(b: Vector3): Vector3 {
		val newX = this.y * b.z - this.z * b.y
		val newY = this.z * b.x - this.x * b.z
		val newZ = this.x * b.y - this.y * b.x
		return Vector3(newX, newY, newZ)
	}
	
	override fun toString(): String {
		return "($x, $y, $z)"
	}
	
	fun data(): DoubleArray {
		return doubleArrayOf(x, y, z)
	}
	
	fun floatData(): FloatArray {
		return floatArrayOf(x.toFloat(), y.toFloat(), z.toFloat())
	}
	
	fun makePerpendicularTo(other: Vector3): Vector3 {
		val oldLength = length()
		return other.cross(this).cross(other).setLength(oldLength)
	}
	
	private fun setLength(newLength: Double): Vector3 {
		return this * (newLength / length())
	}
	
	companion object {
		val NX = Vector3(-1.0, 0.0, 0.0)
		val NY = Vector3(0.0, -1.0, 0.0)
		val NZ = Vector3(0.0, 0.0, -1.0)
		val X = Vector3(1.0, 0.0, 0.0)
		val Y = Vector3(0.0, 1.0, 0.0)
		val Z = Vector3(0.0, 0.0, 1.0)
		val XY = Vector3(1.0, 1.0, 0.0)
		val XZ = Vector3(1.0, 0.0, 1.0)
		val YZ = Vector3(0.0, 1.0, 1.0)
		val XYZ = Vector3(1.0, 1.0, 1.0)
		val ZERO = Vector3(0.0, 0.0, 0.0)
		val ONE = Vector3(1.0, 1.0, 1.0)
	}
	
	val xy: Vector2
		get() = Vector2(x, y)
	val xz: Vector2
		get() = Vector2(x, z)
	val yz: Vector2
		get() = Vector2(y, z)
	
}
