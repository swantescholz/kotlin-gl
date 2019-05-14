package gl.math

import math.linearalgebra.Vector3

class Triangle {
	
	val a = Vertex()
	val b = Vertex()
	val c = Vertex()
	
	constructor() {
		
	}
	
	constructor(a: Vector3, b: Vector3, c: Vector3) {
		this.a.position = a
		this.b.position = b
		this.c.position = c
		setNormalsByPlane()
	}
	
	fun setNormals(normal: Vector3) {
		a.normal = normal
		b.normal = normal
		c.normal = normal
	}
	
	val planeNormal: Vector3
		get() = normal(a.position, b.position, c.position)
	
	fun setNormalsByPlane() {
		setNormals(planeNormal)
	}
	
	fun hasTexCoords(): Boolean {
		return a.hasTexCoord() && b.hasTexCoord() && c.hasTexCoord()
	}
	
	companion object {
		
		fun normal(a: Vector3, b: Vector3, c: Vector3): Vector3 {
			return b.minus(a).cross(c.minus(a)).normalize()
		}
	}
}
