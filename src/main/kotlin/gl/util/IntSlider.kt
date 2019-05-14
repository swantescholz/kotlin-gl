package gl.util

import math.linearalgebra.MathUtil

class IntSlider(private val start: Int, private val end: Int) {
	
	var value: Int = 0
		private set
	
	fun progress(steps: Int) {
		value += steps
		value = MathUtil.clamp(value, start, end)
	}
	
}
