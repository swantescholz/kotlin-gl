package gl.math

import math.linearalgebra.Vector3

interface MovableOrientable : Movable, Orientable {
	
	fun lookAt(to: Vector3) {
		lookInDirection(to - position)
	}
	
	fun moveLocal(v: Vector3) {
		val delta = xAxis.times(v.x).plus(yAxis.times(v.y)).plus(zAxis.times(v.z))
		move(delta)
	}
	
	fun advance(zmove: Double) {
		moveLocal(Vector3(0.0, 0.0, zmove))
	}
	
}
