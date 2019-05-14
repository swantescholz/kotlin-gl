package gl.gfx.font

import gl.gfx.GlUtil
import gl.gfx.gl
import gl.gfx.texture.Texture
import gl.math.MovableOrientable
import math.linearalgebra.Vector3
import javax.media.opengl.GL

class TexturedRectangle(val texture: Texture, val wDivH: Double) : MovableOrientable {
	override var position: Vector3 = Vector3()
	override var direction: Vector3 = Vector3(0, 0, 1)
	override var upVector: Vector3 = Vector3.Y
	
	fun render(height: Double = 1.0) {
		val gl = gl()
		texture.use()
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE)
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE)
		val f = 0.5 * height
		val m = orientationMatrix.times(translationMatrix)
		val a = m.transform(Vector3(-f * wDivH, -f, 0.0))
		val b = m.transform(Vector3(f * wDivH, -f, 0.0))
		val c = m.transform(Vector3(f * wDivH, f, 0.0))
		val d = m.transform(Vector3(-f * wDivH, f, 0.0))
		gl.glBegin(GL.GL_QUADS)
		gl.glTexCoord2i(0, 0)
		GlUtil.direct(a)
		gl.glTexCoord2i(1, 0)
		GlUtil.direct(b)
		gl.glTexCoord2i(1, 1)
		GlUtil.direct(c)
		gl.glTexCoord2i(0, 1)
		GlUtil.direct(d)
		gl.glEnd()
	}
	
	fun dispose() = texture.dispose()
	
}