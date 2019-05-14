package gl.math

import math.linearalgebra.Matrix
import math.linearalgebra.Vector3

interface Movable {
	
	var position: Vector3
	
	fun move(delta: Vector3) {
		position += delta
	}
	
	fun moveTo(newPosition: Vector3) {
		position = newPosition
	}
	
	val translationMatrix: Matrix
		get() = Matrix.translation(position)
	
}
