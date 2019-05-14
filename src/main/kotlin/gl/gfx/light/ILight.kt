package gl.gfx.light

import gl.math.Color
import math.linearalgebra.Vector3

interface ILight {
	
	val id: Int
	val ambient: Color
	val diffuse: Color
	val specular: Color
	val position: Vector3
	val isEnabled: Boolean
	val isPositional: Boolean
	val constantAttenuation: Double
		get() = 1.0
	
	val linearAttenuation: Double
		get() = 0.0
	
	val quadraticAttenuation: Double
		get() = 0.0
	
	val spotCutoff: Double
		get() = 180.0
	
	val spotCosCutoff: Double
		get() = Math.cos(spotCutoff)
	
	val spotExponent: Double
		get() = 0.0
	
	val spotDirection: Vector3
		get() = Vector3(0.0, 0.0, -1.0)
}
