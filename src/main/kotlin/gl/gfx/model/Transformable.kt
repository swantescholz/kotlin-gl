package gl.gfx.model

import math.linearalgebra.Matrix

open class Transformable {
	
	var transformation = Matrix.identity()
	
	fun transform(newTransformation: Matrix) {
		this.transformation = this.transformation.times(newTransformation)
	}
	
}
