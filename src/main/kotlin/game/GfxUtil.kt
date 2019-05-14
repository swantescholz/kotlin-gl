package game

import gl.gfx.GlUtil
import gl.gfx.gl
import gl.gfx.shader.Program
import gl.gfx.shader.ProgramManager
import gl.gfx.texture.Texture
import gl.math.Material
import gl.util.StringUtil
import math.linearalgebra.Coordi
import math.linearalgebra.Vector2
import math.linearalgebra.Vector3
import util.extensions.doTimes
import util.log.Log
import java.io.File
import javax.media.opengl.GL

object GfxUtil {
	
	fun renderQuad(coordinates: Coordi, texture: Texture, z: Double, cwRotations: Int = 0) {
//		val height = 1.0 / texture.dimension.aspectRatio
		texture.use(textureLayerIndex = 0)
		val texCoords = arrayListOf(Vector2(0, 0), Vector2(1, 0), Vector2(1, 1), Vector2(0, 1))
		cwRotations.doTimes { texCoords.add(texCoords[0]); texCoords.removeAt(0) }
		gl().glBegin(GL.GL_QUADS)
		GlUtil.directTexCoord(texCoords[0])
		val lowerLeft = coordinates.vec2
		GlUtil.direct(Vector3(lowerLeft.x, lowerLeft.y, z))
		GlUtil.directTexCoord(texCoords[1])
		GlUtil.direct(Vector3(lowerLeft.x + 1, lowerLeft.y, z))
		GlUtil.directTexCoord(texCoords[2])
		GlUtil.direct(Vector3(lowerLeft.x + 1, lowerLeft.y + 1, z))
		GlUtil.directTexCoord(texCoords[3])
		GlUtil.direct(Vector3(lowerLeft.x, lowerLeft.y + 1, z))
		gl().glEnd()
	}
	
	fun draw2DTexturedQuad(lowerLeft: Vector2, size: Vector2 = Vector2.ONE, z: Double = 0.0) {
		gl().glBegin(GL.GL_QUADS)
		GlUtil.directTexCoord(Vector2(0, 0))
		GlUtil.direct(Vector3(lowerLeft.x, lowerLeft.y, z))
		GlUtil.directTexCoord(Vector2(1, 0))
		GlUtil.direct(Vector3(lowerLeft.x + size.x, lowerLeft.y, z))
		GlUtil.directTexCoord(Vector2(1, 1))
		GlUtil.direct(Vector3(lowerLeft.x + size.x, lowerLeft.y + size.y, z))
		GlUtil.directTexCoord(Vector2(0, 1))
		GlUtil.direct(Vector3(lowerLeft.x, lowerLeft.y + size.y, z))
		gl().glEnd()
	}
	
	fun drawCoordinateSystem() {
		Program.push()
		ProgramManager.instance["color"].use()
		val axisLength = 20.0
		Material.RED.use()
		drawLine(Vector3.NX * axisLength, Vector3.X * axisLength)
		Material.GREEN.use()
		drawLine(Vector3.NY * axisLength, Vector3.Y * axisLength)
		Material.BLUE.use()
		drawLine(Vector3.NZ * axisLength, Vector3.Z * axisLength)
		Program.pop()
	}
	
	fun drawLine(a: Vector3, b: Vector3) {
		gl().glBegin(GL.GL_LINES)
		GlUtil.direct(a)
		GlUtil.direct(b)
		gl().glEnd()
	}
	
	fun saveScreenshot() {
		val file = File("screenshots", StringUtil.currentTimestamp() + ".jpg")
		GlUtil.saveScreenshot(file)
		Log.info("saved screenshot " + file)
	}

//	private fun drawFontRectangleAndHud() {
//		Program.push()
//		textureProgram.use()
//		Hud.renderAllCorners()
//		val x = font.createTexturedRectangle("boobar 1233 yy brOWN FOX JUMPS", Color.CYAN)
//		x.position = Vector3(-0.1, 0.0)
//		GlUtil.toggleProjectionMode()
//		val oldViewMatrix = UniformManager.getViewMatrix()
//		UniformManager.setViewMatrix(Matrix.identity())
//		x.render(height = 0.5)
//		UniformManager.setViewMatrix(oldViewMatrix)
//		GlUtil.toggleProjectionMode()
//		Program.pop()
//	}
//
}