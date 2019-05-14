package math.linearalgebra

class Quaternion : VectorSpace<Quaternion> {
	override fun unaryMinus(): Quaternion = Quaternion(-w, -x, -y, -z)
	
	val w: Double
	val x: Double
	val y: Double
	val z: Double
	
	constructor() {
		w = 0.0
		x = 0.0
		y = 0.0
		z = 0.0
	}
	
	constructor(w: Double, x: Double, y: Double, z: Double) {
		this.w = w
		this.x = x
		this.y = y
		this.z = z
	}
	
	constructor(w: Double, v: Vector3) : this(w, v.x, v.y, v.z) {
	}
	
	override fun plus(o: Quaternion): Quaternion {
		return Quaternion(w + o.w, x + o.x, y + o.y, z + o.z)
	}
	
	override fun minus(o: Quaternion): Quaternion {
		return Quaternion(w - o.w, x - o.x, y - o.y, z - o.z)
	}
	
	override fun times(o: Double): Quaternion {
		return Quaternion(w * o, x * o, y * o, z * o)
	}
	
	fun equal(o: Quaternion): Boolean {
		return w == o.w && x == o.x && y == o.y && z == o.z
	}
	
	override fun almostEqual(o: Quaternion): Boolean {
		return MathUtil.almostEqual(w, o.w) && MathUtil.almostEqual(x, o.x) &&
				MathUtil.almostEqual(y, o.y) && MathUtil.almostEqual(z, o.z)
	}
	
	fun mul(q: Quaternion): Quaternion {
		val a = w * q.w - x * q.x - y * q.y - z * q.z
		val b = x * q.w + w * q.x + y * q.z - z * q.y
		val c = y * q.w + w * q.y + z * q.x - x * q.z
		val d = z * q.w + w * q.z + x * q.y - y * q.x
		return Quaternion(a, b, c, d)
	}
	
	fun conjugate(): Quaternion {
		return Quaternion(w, -x, -y, -z)
	}
	
	fun normalize(): Quaternion {
		return div(length())
	}
	
	fun length(): Double {
		return Math.sqrt(length2())
	}
	
	fun length2(): Double {
		return w * w + x * x + y * y + z * z
	}
	
	fun invert(): Quaternion {
		val inverted = conjugate()
		return inverted.div(inverted.length2())
	}
	
	fun dot(q: Quaternion): Double {
		return w * q.w + x * q.x + y * q.y + z * q.z
	}
	
	val angle: Double
		get() = 2.0 * Math.acos(w)
	
	val axis: Vector3
		get() = Vector3(x, y, z).normalize()
	
	val vector: Vector3
		get() = Vector3(x, y, z)
	
	fun transform(v: Vector3): Vector3 {
		val vpure = Quaternion(0.0, v)
		val result = mul(vpure).mul(invert())
		return result.vector
	}
	
	fun lerp(q: Quaternion, t: Double): Quaternion {
		return this * (1.0 - t) + (q * t)
	}
	
	fun nlerp(q: Quaternion, t: Double): Quaternion {
		return lerp(q, t).normalize()
	}
	
	fun slerp(q: Quaternion, t: Double): Quaternion {
		val alpha = Math.acos(dot(q))
		val tmp = q * (Math.sin(t * alpha))
		return times(Math.sin((1.0 - t) * alpha)).plus(tmp).div(Math.sin(alpha))
	}
	
	fun data(): DoubleArray {
		return doubleArrayOf(x, y, z)
	}
	
	override fun toString(): String {
		return "($w, $x, $y, $z)"
	}
	
	fun toMatrix(): Matrix {
		val xx2 = 2.0 * x * x
		val yy2 = 2.0 * y * y
		val zz2 = 2.0 * z * z
		val wx2 = 2.0 * w * x
		val wy2 = 2.0 * w * y
		val wz2 = 2.0 * w * z
		val xy2 = 2.0 * x * y
		val xz2 = 2.0 * x * z
		val yz2 = 2.0 * y * z
		val a = DoubleArray(16)
		a[15] = 1.0
		a[3] = 0.0
		a[7] = 0.0
		a[11] = 0.0
		a[12] = 0.0
		a[13] = 0.0
		a[14] = 0.0
		a[0] = 1.0 - yy2 - zz2
		a[5] = 1.0 - xx2 - zz2
		a[10] = 1.0 - xx2 - yy2
		a[1] = xy2 - wz2
		a[4] = xy2 + wz2
		a[8] = xz2 - wy2
		a[2] = xz2 + wy2
		a[6] = yz2 - wx2
		a[9] = yz2 + wx2
		return Matrix(a)
	}
	
	companion object {
		
		val ZERO = Quaternion(0.0, 0.0, 0.0, 0.0)
		val ONE = Quaternion(1.0, 0.0, 0.0, 0.0)
		
		fun axisAngle(axis: Vector3, angle: Double): Quaternion {
			return Quaternion(Math.cos(-angle * 0.5),
					axis.normalize().times(Math.sin(-angle * 0.5)))
		}
	}
	
}
