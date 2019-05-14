package gl.input

import gl.gfx.GlUtil
import math.linearalgebra.Vector2
import java.awt.event.*
import java.util.*

class Mouse : MouseListener, MouseMotionListener, MouseWheelListener {
	
	private val buttonMap = HashMap<Int, MouseButton>()
	private var currentPosition = Vector2.ZERO
	
	override fun mousePressed(e: MouseEvent) {
		val button = getButton(e.button)
		button.state = ButtonState.PRESSED
		button.positionOnLastPress = getMouseLocation(e)
	}
	
	private fun getMouseLocation(e: MouseEvent): Vector2 {
		return Vector2(e.x.toDouble(), e.y.toDouble())
	}
	
	
	override fun mouseReleased(e: MouseEvent) {
		val button = getButton(e.button)
		button.state = ButtonState.RELEASED
	}
	
	override fun mouseWheelMoved(e: MouseWheelEvent) {
		
	}
	
	private fun checkButton(buttonId: Int) {
		if (!buttonMap.containsKey(buttonId)) {
			buttonMap.put(buttonId, MouseButton())
		}
	}
	
	fun getButton(buttonId: Int): MouseButton {
		checkButton(buttonId)
		return buttonMap[buttonId]!!
	}
	
	fun wasPressed(buttonId: Int): Boolean {
		checkButton(buttonId)
		return buttonMap[buttonId]!!.wasPressed()
	}
	
	fun wasReleased(buttonId: Int): Boolean {
		checkButton(buttonId)
		return buttonMap[buttonId]!!.wasReleased()
	}
	
	fun isDown(buttonId: Int): Boolean {
		checkButton(buttonId)
		return buttonMap[buttonId]!!.isDown
	}
	
	override fun mouseClicked(e: MouseEvent) {
	}
	
	override fun mouseEntered(e: MouseEvent) {
	}
	
	override fun mouseExited(e: MouseEvent) {
	}
	
	override fun mouseDragged(e: MouseEvent) {
		currentPosition = getMouseLocation(e)
	}
	
	override fun mouseMoved(e: MouseEvent) {
		currentPosition = getMouseLocation(e)
	}
	
	// gives x and y values normalized from range [-1, 1]
	val currentNormalizedScreenCoordinates: Vector2
		get() {
			val dimension = GlUtil.dimension
			val w = dimension.width.toDouble()
			val h = dimension.height.toDouble()
			val x = 2 * currentPosition.x / (w - 1) - 1
			val y = 2 * (h - currentPosition.y) / (h - 1) - 1
			return Vector2(x, y)
		}
	
}
