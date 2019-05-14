package math.linearalgebra

class Plane : VectorSpace<Plane> {
	
	val n: Vector3
	val d: Double
	
	
	constructor() {
		n = Vector3.Y
		d = 0.0
	}
	
	constructor(x: Double, y: Double, z: Double, distance: Double) : this(Vector3(x, y, z), distance) {
	}
	
	constructor(normal: Vector3, distance: Double) {
		this.n = normal
		this.d = distance
	}
	
	override fun unaryMinus(): Plane = Plane(-n, -d)
	
	override fun plus(o: Plane): Plane {
		return Plane(n + o.n, d + o.d)
	}
	
	override fun minus(o: Plane): Plane {
		return Plane(n - o.n, d - o.d)
	}
	
	override fun times(o: Double): Plane {
		return Plane(n * o, d * o)
	}
	
	fun equal(o: Plane): Boolean {
		return n.equals(o.n) && d == o.d
	}
	
	override fun almostEqual(o: Plane): Boolean {
		return n.almostEqual(o.n) && MathUtil.almostEqual(d, o.d)
	}
	
	fun normalize(): Plane {
		val lsq = n.length2()
		if (MathUtil.almostEqual(lsq, 1.0)) {
			return this
		}
		return div(Math.sqrt(lsq))
	}
	
	fun dot(b: Vector3): Double {
		return n.dot(b) + d
	}
	
	fun dotNormal(b: Vector3): Double {
		return n.dot(b)
	}
	
	fun distance(point: Vector3): Double {
		return dot(point)
	}
	
	fun nearestPoint(point: Vector3): Vector3 {
		return -n * (dot(point)) + point
	}
	
	companion object {
		
		val X = Plane(1.0, 0.0, 0.0, 0.0)
		val Y = Plane(0.0, 1.0, 0.0, 0.0)
		val Z = Plane(0.0, 0.0, 1.0, 0.0)
		val XY = Plane(1.0, 1.0, 0.0, 0.0)
		val XZ = Plane(1.0, 0.0, 1.0, 0.0)
		val YZ = Plane(0.0, 1.0, 1.0, 0.0)
		val XYZ = Plane(1.0, 1.0, 1.0, 0.0)
		
		fun fromPoints(a: Vector3, b: Vector3, c: Vector3): Plane {
			val n = (b - a).cross(c - a).normalize()
			val d = n.dot(a)
			return Plane(n, d)
		}
	}
}
