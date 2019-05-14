package game

import gl.input.Keyboard
import math.linearalgebra.Coordi
import util.extensions.max
import util.extensions.min
import java.awt.event.KeyEvent

class RoomFocus(val numberOfRooms: Int, val levelName: String, val undoManager: UndoManager) {
	companion object {
		val metaLayoutWidth = 3
		val dayNames = arrayListOf("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "2nd Saturday", "2nd Sunday", "2nd Monday", "2nd Tuesday", "2nd Wednesday",
				"2nd Thursday", "2nd Friday")
		
		fun getNameForRoomIndex(roomIndex: Int): String = dayNames[roomIndex]
		fun coordToIndex(coordi: Coordi): Int = metaLayoutWidth * coordi.y + coordi.x
	}
	
	init {
		updateHud()
	}
	
	val currentMetaLayoutHeight = (numberOfRooms + metaLayoutWidth - 1) / metaLayoutWidth
	var currentFocusIndex: Int = 0
		private set
	val currentFocusCoord: Coordi
		get() = indexToCoord(currentFocusIndex)
	
	fun indexToCoord(roomIndex: Int) = Coordi(roomIndex % metaLayoutWidth, roomIndex / metaLayoutWidth)
	
	fun update(keyboard: Keyboard) {
		listOf(KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A).
				indexOfFirst { keyboard.wasPressed(it) }.let { keyIndex ->
			var (x, y) = currentFocusCoord
			when (keyIndex) {
				2 -> y = (currentMetaLayoutHeight - 1).min(y + 1)
				0 -> y = 0.max(y - 1)
				3 -> x = 0.max(x - 1)
				1 -> x = (metaLayoutWidth - 1).min(x + 1)
				else -> return@let
			}
			val topLevelNumRooms = (numberOfRooms - 1) % metaLayoutWidth
			if (y == currentMetaLayoutHeight - 1 && x > topLevelNumRooms)
				x = topLevelNumRooms
			val oldFocusIndex = currentFocusIndex
			undoManager.addActionToGroup({
				currentFocusIndex = y * metaLayoutWidth + x
				updateHud()
			}, {
				currentFocusIndex = oldFocusIndex
				updateHud()
			})
		}
	}
	
	private fun updateHud() {
		Hud.upperRight.addLine("$levelName - ${getNameForRoomIndex(currentFocusIndex)}")
	}
	
}