package gl.gfx.texture

import gl.gfx.gl
import gl.gfx.shader.uniform.UniformManager
import gl.math.Dimension
import gl.util.DimensionedObject
import math.linearalgebra.Matrix
import javax.media.opengl.GL

class Texture(private val data: TextureData) : DimensionedObject {
	override val dimension: Dimension
		get() = data.dimension
	
	internal var transformation = Matrix.identity()
	
	fun dispose() {
		data.dispose()
	}
	
	fun use(textureLayerIndex: Int = 0) {
		UniformManager.setTexture(this, textureLayerIndex)
	}
	
	// use the function *use*, binding should not be happening in game code
	internal fun bind() {
		data.bind()
	}
	
	companion object {
		
		val MAX_LAYERS = 4
		
		fun enable() {
			gl().glEnable(GL.GL_TEXTURE_2D)
		}
		
		fun disable() {
			gl().glDisable(GL.GL_TEXTURE_2D)
		}
		
		fun unbind() {
			TextureData.unbind()
		}
	}
	
}
