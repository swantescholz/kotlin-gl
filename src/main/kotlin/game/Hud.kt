package game

import gl.gfx.GlUtil
import gl.gfx.font.Font
import gl.gfx.font.TexturedRectangle
import gl.gfx.shader.Program
import gl.gfx.shader.ProgramManager
import gl.gfx.shader.uniform.UniformManager
import gl.math.Color
import math.linearalgebra.Matrix
import math.linearalgebra.Vector3
import util.Config
import util.extensions.int
import java.util.*


object Hud {
	var textColor = Color.WHITE
	var textHeight = 0.07
	
	class Corner(val maxLines: Int, val rightAligned: Boolean, val isOnUpperSide: Boolean) {
		private class Line(val text: String, val rectangle: TexturedRectangle) {
			fun destroy() = rectangle.dispose()
		}
		
		private val lines = ArrayList<Line>()
		fun clear() {
			lines.forEach { it.destroy() }
			lines.clear()
		}
		fun addLine(lineText: String) {
			lines.add(Line(lineText,
					Font.DEFAULT.createTexturedRectangle(lineText, textColor)))
			while (lines.size > maxLines) {
				lines[0].destroy()
				lines.removeAt(0)
			}
		}
		
		internal fun render() {
			var y = (if (isOnUpperSide) 1.0 else -1.0 + lines.size * textHeight) - 0.5 * textHeight
			lines.forEach {
				val lineWidth = textHeight * it.rectangle.wDivH
				val x = if (rightAligned) 1 * GlUtil.aspectRatio - lineWidth * 0.5 else -1 * GlUtil.aspectRatio + lineWidth * 0.5
				it.rectangle.position = Vector3(x, y)
				it.rectangle.render(textHeight)
				y -= textHeight
			}
		}
	}
	
	val upperLeft = Corner(8, false, true)
	val upperRight = Corner(1, true, true)
	val lowerLeft = Corner(Config.game["console.lines.max"].int, false, false)
	val lowerRight = Corner(Config.game["console.lines.max"].int, true, false)
	
	// some simple texture program must be active
	fun renderAllCorners() {
		Program.push()
		ProgramManager.instance["texture"].use()
		GlUtil.toggleProjectionMode()
		val oldViewMatrix = UniformManager.getViewMatrix()
		UniformManager.setViewMatrix(Matrix.identity())
		upperLeft.render()
		upperRight.render()
		lowerLeft.render()
		lowerRight.render()
		UniformManager.setViewMatrix(oldViewMatrix)
		GlUtil.toggleProjectionMode()
		Program.pop()
	}
}