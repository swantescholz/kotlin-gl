package gl.gfx

import gl.gfx.shader.uniform.UniformManager
import gl.math.Color
import gl.math.Dimension
import math.linearalgebra.*
import util.log.Log
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.IntBuffer
import javax.imageio.ImageIO
import javax.media.opengl.GL

//returns current running glInstance or throws null pointer exception otherwise
fun gl(): GL = GlUtil.glInstance!!

object GlUtil {
	
	val DEFAULT_FOV = MathUtil.toRadian(80.0)
	
	var glInstance: GL? = null
		private set(value) {
			field = value
			GlOperator.updateAll()
		}
	private var wireframed = false
	var dimension = Dimension(0, 0)
	private var useOrthoProjection = false
	
	
	fun toArray(i: Int): IntArray {
		val tmp = IntArray(1)
		tmp[0] = i
		return tmp
	}
	
	fun toggleWireframeMode() {
		if (wireframed) {
			glInstance!!.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL)
		} else {
			glInstance!!.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE)
		}
		wireframed = !wireframed
	}
	
	fun toggleShadeModel() {
		val model = getInteger(GL.GL_SHADE_MODEL)
		var newModel = GL.GL_FLAT
		if (model == GL.GL_FLAT) {
			newModel = GL.GL_SMOOTH
		}
		glInstance!!.glShadeModel(newModel)
	}
	
	fun toggleEnabled(field: Int) {
		if (glInstance!!.glIsEnabled(field)) {
			glInstance!!.glDisable(field)
		} else {
			glInstance!!.glEnable(field)
		}
	}
	
	private fun getInteger(field: Int): Int {
		val buffer = IntBuffer.allocate(1)
		glInstance!!.glGetIntegerv(field, buffer)
		return buffer.get()
	}
	
	fun setClearColor(bgColor: Color) {
		glInstance!!.glClearColor(bgColor.r.toFloat(), bgColor.g.toFloat(), bgColor.b.toFloat(), bgColor.a.toFloat())
	}
	
	fun initGl(gl: GL) {
		glInstance = gl
		setClearColor(Color.SKY)
		gl.glClearDepth(1.0)
		
		gl.glEnable(GL.GL_DEPTH_TEST)
		gl.glDepthFunc(GL.GL_LEQUAL)
		
		gl.glCullFace(GL.GL_BACK)
		gl.glFrontFace(GL.GL_CCW)
		gl.glEnable(GL.GL_BLEND)
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA)
	}
	
	val aspectRatio: Double
		get() = dimension.aspectRatio
	
	private fun setDimension(width: Int, height: Int) {
		dimension = Dimension(width, height)
	}
	
	fun reshapeGlView(gl: GL, x: Int, y: Int, width: Int, height: Int) {
		glInstance = gl
		Log.debug("reshaping: $x, $y, $width, $height")
		gl.glViewport(0, 0, width, height)
		setDimension(width, height)
		updateProjectionMatrix()
	}
	
	private fun updateProjectionMatrix() {
		if (useOrthoProjection) {
			val top = 1.0
			val right = top * aspectRatio
			val far = top
			val ortho = Matrix.ortho(-right, right, -top, top, -far, far)
			UniformManager.setProjectionMatrix(ortho)
		} else {
			val projectionMatrix = Matrix.projection(DEFAULT_FOV, aspectRatio, 1.01, 1000.0)
			UniformManager.setProjectionMatrix(projectionMatrix)
		}
	}
	
	fun computeCameraLocalScreenRayDirection(normalizedScreenCoordinates: Vector2): Vector3 {
		val xMax = Math.tan(DEFAULT_FOV * 0.5)
		val yMax = xMax / aspectRatio
		return Vector3(normalizedScreenCoordinates.x * xMax, normalizedScreenCoordinates.y * yMax, -1.0)
	}
	
	fun toggleProjectionMode() {
		useOrthoProjection = !useOrthoProjection
		updateProjectionMatrix()
	}
	
	fun initFrame(gl: GL) {
		glInstance = gl
		gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)
	}
	
	val viewportDimension: Dimension
		get() {
			val data = IntArray(4)
			glInstance!!.glGetIntegerv(GL.GL_VIEWPORT, data, 0)
			return Dimension(data[2], data[3])
		}
	
	fun saveScreenshot(file: File) {
		val image = readScreenshot()
		try {
			file.mkdirs()
			ImageIO.write(image, "jpg", file)
		} catch (e: IOException) {
			e.printStackTrace()
		}
		
	}
	
	fun readScreenshot(): BufferedImage {
		val viewportDimension = viewportDimension
		val bytesPerPixel = 3
		Log.debug("screenshot dimension: " + viewportDimension)
		val buffer = ByteBuffer.allocate(viewportDimension.size * bytesPerPixel)
		glInstance!!.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1)
		glInstance!!.glReadBuffer(GL.GL_FRONT)
		glInstance!!.glReadPixels(0, 0, viewportDimension.width, viewportDimension.height,
				GL.GL_RGB, GL.GL_UNSIGNED_BYTE, buffer)
		val image = BufferedImage(viewportDimension.width,
				viewportDimension.height, BufferedImage.TYPE_INT_RGB)
		for (y in 0..viewportDimension.height - 1) {
			for (x in 0..viewportDimension.width - 1) {
				val index = ((viewportDimension.height - y - 1) * viewportDimension.width + x) * bytesPerPixel
				val red = buffer.get(index)
				val green = buffer.get(index + 1)
				val blue = buffer.get(index + 2)
				val rgb = Color.rgbIntFromBytes(red, green, blue)
				image.setRGB(x, y, rgb)
			}
		}
		return image
	}
	
	fun pushEnabled() {
		glInstance!!.glPushAttrib(GL.GL_ENABLE_BIT)
	}
	
	fun popEnabled() {
		glInstance!!.glPopAttrib()
	}
	
	fun direct(vector2: Vector2) {
		glInstance!!.glVertex2d(vector2.x, vector2.y)
	}
	
	fun direct(vector3: Vector3) {
		glInstance!!.glVertex3d(vector3.x, vector3.y, vector3.z)
	}
	
	fun direct(vector4: Vector4) {
		glInstance!!.glVertex4d(vector4.x, vector4.y, vector4.z, vector4.w)
	}
	
	fun directTexCoord(vector2: Vector2) {
		gl().glTexCoord2d(vector2.x, vector2.y)
	}
	
}
