package gl.gfx.light

import gl.math.Movable
import math.linearalgebra.Vector3

class PositionalLight : Light(), Movable {
	
	override var position = Vector3()
	override var constantAttenuation = 1.0
	override var linearAttenuation = 0.0
	override var quadraticAttenuation = 0.0
	
	override val isPositional: Boolean
		get() = true
	
}
