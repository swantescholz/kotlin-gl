package gl.input

import math.linearalgebra.Vector2

class MouseButton {
	internal var state = ButtonState.UP
	internal var positionOnLastPress: Vector2? = null
	
	val isDown: Boolean
		get() = state == ButtonState.DOWN || state == ButtonState.PRESSED
	
	fun wasPressed(): Boolean {
		if (state == ButtonState.PRESSED) {
			state = ButtonState.DOWN
			return true
		}
		return false
	}
	
	fun wasReleased(): Boolean {
		if (state == ButtonState.RELEASED) {
			state = ButtonState.UP
			return true
		}
		return false
	}
	
}
