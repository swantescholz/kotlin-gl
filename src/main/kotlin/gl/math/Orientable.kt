package gl.math

import math.linearalgebra.Matrix
import math.linearalgebra.Vector3

interface Orientable {
	
	var upVector: Vector3
	
	var direction: Vector3
	
	fun lookInDirection(newDirection: Vector3) {
		direction = newDirection
		upVector = upVector.makePerpendicularTo(newDirection)
	}
	
	val xAxis: Vector3
		get() = upVector.cross(direction).normalize()
	
	val yAxis: Vector3
		get() = zAxis.cross(xAxis).normalize()
	
	val zAxis: Vector3
		get() = direction.normalize()
	
	val orientationMatrix: Matrix
		get() = Matrix.rotationFromAxes(xAxis, yAxis, zAxis)
	
	fun pitch(radian: Double) { //x
		val r = -radian //for CCW
		val rotation = Matrix.rotation(xAxis, r)
		direction = rotation.transformNormal(direction)
		upVector = rotation.transformNormal(upVector)
	}
	
	fun yaw(radian: Double) { //y
		val r = -radian //for CCW
		direction = Matrix.rotation(upVector, r).transformNormal(direction)
	}
	
	fun yawAroundOtherUpVector(radian: Double, otherUpVector: Vector3) {
		val r = -radian //for CCW
		val rotation = Matrix.rotation(otherUpVector, r)
		direction = rotation.transformNormal(direction)
		upVector = rotation.transformNormal(upVector)
	}
	
	fun roll(radian: Double) { //z
		var radian = radian
		radian *= -1.0 //for CCW
		upVector = Matrix.rotation(direction, radian).transformNormal(upVector)
	}
	
	companion object {
		
		val DEFAULT_FRONT_DIRECTION = Vector3.NZ
		val DEFAULT_UP_VECTOR = Vector3.Y
	}
	
}
