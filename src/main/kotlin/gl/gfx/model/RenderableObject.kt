package gl.gfx.model

import gl.gfx.shader.uniform.UniformManager
import gl.math.MovableOrientable
import math.linearalgebra.Matrix
import math.linearalgebra.Vector3

// updates its transformation matrix automatically when any info is changed
abstract class RenderableObject : MovableOrientable, Scalable, Renderable {
	override var position: Vector3 = Vector3()
		set(value) {
			field = value
			updateTransformationMatrix()
		}
	override var upVector: Vector3 = Vector3.Y
		set(value) {
			field = value
			updateTransformationMatrix()
		}
	override var direction: Vector3 = Vector3.Z
		set(value) {
			field = value
			updateTransformationMatrix()
		}
	override var scaling: Vector3 = Vector3.ONE
		set(value) {
			field = value
			updateTransformationMatrix()
		}
	var transformation: Matrix = Matrix.identity()
		private set
	
	init {
		updateTransformationMatrix()
	}
	
	private fun updateTransformationMatrix() {
		transformation = scalingMatrix * orientationMatrix * translationMatrix
	}
	
	override fun render() {
		val oldModelMatrix = UniformManager.getModelMatrix()
		UniformManager.setModelMatrix(oldModelMatrix.times(transformation))
		draw()
		UniformManager.setModelMatrix(oldModelMatrix)
	}
	
	protected abstract fun draw()
	fun copyTransformationsFromOtherRenderableObject(o: RenderableObject) {
		position = o.position
		direction = o.direction
		upVector = o.upVector
		scaling = o.scaling
	}
	
}
