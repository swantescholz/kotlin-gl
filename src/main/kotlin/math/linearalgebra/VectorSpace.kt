package math.linearalgebra

import util.extensions.pow

abstract class VectorSpace<T : VectorSpace<T>> {
	
	abstract operator fun unaryMinus(): T
	
	abstract operator fun plus(o: T): T
	
	abstract operator fun minus(o: T): T
	
	abstract operator fun times(o: Double): T
	operator fun times(o: Int): T = this * o.toDouble()
	operator fun times(o: Long): T = this * o.toDouble()
	operator fun times(o: Float): T = this * o.toDouble()
	
	abstract fun almostEqual(o: T): Boolean
	
	operator fun div(o: Double): T {
		return this * (1.0 / o)
	}
	
	fun interpolateLinear(b: T, t: Double): T {
		return (1.0 - t) * this + b * t
	}
	
	fun interpolateHermite(tangentA: T, valueB: T, tangentB: T, t: Double): T {
		val a = ((this - valueB) * 2.0 + tangentA + tangentB) * (t * t * t)
		val b = ((this - valueB) * -3.0 - tangentA * 2.0 - tangentB) * (t * t)
		return this + a + b + tangentA * t
	}
	
	fun interpolateBilinear(b: T, c: T, d: T, x: Double, y: Double): T {
		val p = this.interpolateLinear(b, x)
		val q = c.interpolateLinear(d, x)
		return p.interpolateLinear(q, y)
	}
	
	fun interpolateExponentially(destination: T, timeForHalvingDistance: Double, elapsedTime: Double): T {
		return this.interpolateLinear(destination, 1.0 - 0.5.pow(elapsedTime / timeForHalvingDistance))
	}
}

operator fun <T : VectorSpace<T>> Double.times(you: VectorSpace<T>): T = you * this
operator fun <T : VectorSpace<T>> Int.times(you: VectorSpace<T>): T = you * this.toDouble()
operator fun <T : VectorSpace<T>> Long.times(you: VectorSpace<T>): T = you * this.toDouble()
operator fun <T : VectorSpace<T>> Float.times(you: VectorSpace<T>): T = you * this.toDouble()