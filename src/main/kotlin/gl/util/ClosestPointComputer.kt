package gl.util

import math.linearalgebra.Vector2

class ClosestPointComputer(private val center: Vector2) {
	var closestPoint: Vector2? = null
		private set
	
	fun update(point: Vector2?) {
		if (point == null) {
			return
		}
		if (closestPoint == null || center.distanceTo2(closestPoint!!) >
				center.distanceTo2(point)) {
			closestPoint = point
		}
	}
}
