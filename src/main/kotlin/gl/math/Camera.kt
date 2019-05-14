package gl.math


import gl.gfx.GlUtil
import gl.gfx.shader.uniform.UniformManager
import math.linearalgebra.Matrix
import math.linearalgebra.Vector2
import math.linearalgebra.Vector3

class Camera constructor(pos: Vector3 = Vector3(0.0, 0.0, 10.0),
                         dir: Vector3 = Vector3(0.0, 0.0, -1.0),
                         up: Vector3 = Vector3(0.0, 1.0, 0.0))
: AbstractMovableOrientable(pos, dir, up) {
	
	val cameraMatrix: Matrix
		get() = Matrix.camera(position, direction, upVector)
	
	fun apply() {
		UniformManager.setCamera(this)
	}
	
	// returns the real world direction of the ray pointed to by the given coordinates
	fun computeScreenRayDirection(normalizedScreenCoordinates: Vector2): Vector3 {
		val localDirection = GlUtil.computeCameraLocalScreenRayDirection(normalizedScreenCoordinates)
		val absoluteDirection = (-xAxis * localDirection.x + yAxis * localDirection.y -
				zAxis * localDirection.z)
		return absoluteDirection.normalize()
	}
	
	companion object {
		val DEFAULT = Camera(Vector3.ZERO, Vector3.NZ, Vector3.Y)
		val DEFAULT_UP_VECTOR = Vector3.Y
	}
}
