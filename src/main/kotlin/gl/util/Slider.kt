package gl.util

import math.linearalgebra.MathUtil

class Slider constructor(private val speed: Double = 1.0, private val start: Double = 0.0, private val end: Double = 1.0) {
	
	var value: Double = 0.toDouble()
	
	constructor(start: Double, end: Double) : this(end - start, start, end) {
	}
	
	init {
		this.value = this.start
	}
	
	fun progress(elapsed: Double) {
		value += elapsed * speed
		value = MathUtil.clamp(value, start, end)
	}
	
	fun map(min: Double, max: Double): Double {
		return MathUtil.interpolate(min, max, unitValue)
	}
	
	val unitValue: Double
		get() = (value - start) / (end - start)
}
