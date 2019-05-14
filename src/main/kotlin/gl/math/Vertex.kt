package gl.math

import math.linearalgebra.Vector2
import math.linearalgebra.Vector3

class Vertex {
	
	var position = Vector3()
	var normal = Vector3(0.0, 1.0, 0.0)
	var texCoord: Vector2? = null
	
	constructor(position: Vector3, normal: Vector3) {
		this.position = position
		this.normal = normal
	}
	
	constructor(position: Vector3, normal: Vector3, texCoord: Vector2) : this(position, normal) {
		this.texCoord = texCoord
	}
	
	constructor() {
	}
	
	fun hasTexCoord(): Boolean {
		return texCoord != null
	}
}
