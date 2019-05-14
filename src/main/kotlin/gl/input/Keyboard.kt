package gl.input

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*

class Keyboard : KeyListener {
	private val keyMap = HashMap<Int, ButtonState>()
	
	override fun keyTyped(e: KeyEvent) {
		
	}
	
	override fun keyPressed(e: KeyEvent) {
		keyMap.put(e.keyCode, ButtonState.PRESSED)
	}
	
	override fun keyReleased(e: KeyEvent) {
		keyMap.put(e.keyCode, ButtonState.RELEASED)
	}
	
	fun wasPressed(keycode: Int): Boolean {
		if (!keyMap.containsKey(keycode)) {
			return false
		}
		val state = keyMap[keycode]
		if (state == ButtonState.PRESSED) {
			keyMap.put(keycode, ButtonState.DOWN)
			return true
		}
		return false
	}
	
	fun isDown(keycode: Int): Boolean {
		if (!keyMap.containsKey(keycode)) {
			return false
		}
		val state = keyMap[keycode]
		return state == ButtonState.DOWN || state == ButtonState.PRESSED
	}
	
	
}
