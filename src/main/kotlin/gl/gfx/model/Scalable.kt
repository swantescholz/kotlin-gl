package gl.gfx.model

import math.linearalgebra.Matrix
import math.linearalgebra.Vector3

interface Scalable {
	var scaling: Vector3
	
	fun scale(factor: Double) = scale(factor, factor, factor)
	
	fun scale(xFactor: Double, yFactor: Double, zFactor: Double) = scale(Vector3(xFactor, yFactor, zFactor))
	
	fun scale(factors: Vector3) {
		scaling = Vector3(scaling.x * factors.x, scaling.y * factors.y, scaling.z * factors.z)
	}
	
	val scalingMatrix: Matrix
		get() = Matrix.scaling(scaling)
}