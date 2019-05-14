package gl.gfx.texture

import gl.gfx.GlUtil
import gl.gfx.gl
import gl.math.Dimension
import gl.util.DimensionedObject
import javax.media.opengl.GL

class TextureData(private val grid: PixelGrid, createMipmaps: Boolean = true) : DimensionedObject {
	private var id: Int = 0
	
	init {
		createId()
		loadIntoGl(createMipmaps)
	}
	
	fun dispose() {
		val gl = gl()
		gl.glDeleteTextures(1, GlUtil.toArray(id), 0)
	}
	
	fun bind() {
		val gl = gl()
		gl.glBindTexture(GL.GL_TEXTURE_2D, id)
	}
	
	override fun toString(): String {
		var str = ""
		str += grid.toString()
		return str
	}
	
	private fun createId() {
		val gl = gl()
		val arr = IntArray(1)
		gl.glGenTextures(1, arr, 0)
		id = arr[0]
	}
	
	private fun loadIntoGl(createMipmaps: Boolean) {
		bind()
		val gl = gl()
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE.toFloat())
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE)
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE)
		if (createMipmaps) {
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR)
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR)
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, grid.width, grid.height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, grid.toBuffer())
			gl.glGenerateMipmapEXT(GL.GL_TEXTURE_2D)
		} else {
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST)
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST)
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, grid.width, grid.height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, grid.toBuffer())
		}
		unbind()
	}
	
	override val dimension: Dimension
		get() = grid.dimension
	
	companion object {
		
		fun unbind() {
			val gl = gl()
			gl.glBindTexture(GL.GL_TEXTURE_2D, 0)
		}
	}
	
}
