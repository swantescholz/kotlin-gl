package math.linearalgebra

class Vector4 : VectorSpace<Vector4> {
	override fun unaryMinus(): Vector4 = Vector4(-x, -y, -z, -w)
	
	val x: Double
	val y: Double
	val z: Double
	val w: Double
	
	constructor() {
		x = 0.0
		y = 0.0
		z = 0.0
		w = 0.0
	}
	
	constructor(x: Double, y: Double, z: Double, w: Double) {
		this.x = x
		this.y = y
		this.z = z
		this.w = w
	}
	
	constructor(v: Vector3, w: Double = 1.0) {
		this.x = v.x
		this.y = v.y
		this.z = v.z
		this.w = w
	}
	
	fun xyz(): Vector3 {
		return Vector3(x, y, z)
	}
	
	fun length(): Double {
		return Math.sqrt(length2())
	}
	
	fun length2(): Double {
		return x * x + y * y + z * z + w * w
	}
	
	fun dot(that: Vector4): Double {
		return x * that.x + y * that.y + z * that.z + w * that.w
	}
	
	fun distanceTo(that: Vector4): Double {
		return Math.sqrt(dot(that))
	}
	
	fun distanceTo2(that: Vector4): Double {
		return dot(that)
	}
	
	override fun plus(o: Vector4): Vector4 {
		return Vector4(x + o.x, y + o.y, z + o.z, w + o.w)
	}
	
	override fun minus(o: Vector4): Vector4 {
		return Vector4(x - o.x, y - o.y, z - o.z, w - o.w)
	}
	
	override fun times(o: Double): Vector4 {
		return Vector4(x * o, y * o, z * o, w * o)
	}
	
	fun equal(o: Vector4): Boolean {
		return x == o.x && y == o.y && z == o.z && w == o.w
	}
	
	override fun almostEqual(o: Vector4): Boolean {
		return MathUtil.almostEqual(x, o.x) && MathUtil.almostEqual(y, o.y) && MathUtil.almostEqual(z, o.z)
				&& MathUtil.almostEqual(w, o.w)
	}
	
	fun normalize(): Vector4 {
		val lsq = length2()
		if (MathUtil.almostEqual(lsq, 1.0)) {
			return this
		}
		return div(Math.sqrt(lsq))
	}
	
	fun data(): DoubleArray {
		return doubleArrayOf(x, y, z, w)
	}
	
	fun floatData(): FloatArray {
		return floatArrayOf(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())
	}
	
	override fun toString(): String {
		return "($x, $y, $z, $w)"
	}
	
	private fun setLength(newLength: Double): Vector4 {
		return times(newLength / length())
	}
	
	fun mul(that: Vector4): Vector4 {
		return Vector4(x * that.x, y * that.y, z * that.z, w * that.w)
	}
	
	operator fun div(that: Vector4): Vector4 {
		return Vector4(x / that.x, y / that.y, z / that.z, w / that.w)
	}
}