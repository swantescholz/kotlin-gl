package game

import gl.input.Keyboard
import math.linearalgebra.Coordi
import math.linearalgebra.Vector2
import util.Config
import util.FileUtil
import util.extensions.int
import util.extensions.max
import util.extensions.sequences.seq
import util.log.Log
import java.awt.event.KeyEvent
import java.io.File
import java.util.*


class Level {
	
	private val rooms = ArrayList<Room>()
	private val undoManager = UndoManager()
	val levelName: String
	private val playerStatus = PlayerStatus()
	
	val done: Boolean
		get() = rooms.all(Room::done)
	val realCurrentPlayerPosition: Vector2
		get() = rooms[roomFocus.currentFocusIndex].computeRealFocusedPlayerPosition(
				computeOffsetForRoom(roomFocus.currentFocusIndex))
	private val roomFocus: RoomFocus
	private var totalWidth: Int = 0
	private var totalHeight: Int = 0
	
	val maxNumberOfRooms: Int
	val numberOfShownRooms: Int
	
	constructor(levelDirectory: File, numberOfShownRooms: Int = 1) {
		this.numberOfShownRooms = numberOfShownRooms
		PlayerInventory.reset()
		val config = Config(File(levelDirectory, "config.txt"))
		config.reload()
		levelName = config["level.name"]
		maxNumberOfRooms = config["number.of.rooms"].int
		roomFocus = RoomFocus(numberOfShownRooms, levelName, undoManager)
		for (roomIndex in 0..numberOfShownRooms - 1) {
			val roomFilename = "room${roomIndex + 1}.txt"
			val room = Room(roomIndex, roomFocus, undoManager, playerStatus)
			room.loadFromString(FileUtil.readFile(File(levelDirectory, roomFilename)))
			rooms.add(room)
			val offset = computeOffsetForRoom(roomIndex)
			totalWidth = totalWidth.max(offset.x + room.width)
			totalHeight = totalHeight.max(-offset.y)
		}
	}
	
	fun update(keyboard: Keyboard) {
		checkUndoRedo(keyboard)
		undoManager.startPotentialActionGroup()
		roomFocus.update(keyboard)
		PlayerInventory.startNewStep()
		updateRooms(keyboard)
		undoManager.endPotentialActionGroup()
	}
	
	private fun checkUndoRedo(keyboard: Keyboard) {
		if (keyboard.wasPressed(KeyEvent.VK_Q))
			undoManager.redo()
		else if (keyboard.wasPressed(KeyEvent.VK_E))
			undoManager.undo()
	}
	
	fun updateRooms(keyboard: Keyboard) {
		if (done || playerStatus.isDead) {
			return
		}
		val playerAction = when {
			keyboard.wasPressed(KeyEvent.VK_UP) -> PlayerAction.UP
			keyboard.wasPressed(KeyEvent.VK_RIGHT) -> PlayerAction.RIGHT
			keyboard.wasPressed(KeyEvent.VK_DOWN) -> PlayerAction.DOWN
			keyboard.wasPressed(KeyEvent.VK_LEFT) -> PlayerAction.LEFT
			else -> return
		}
		for (room in rooms) {
			if (!room.isFocused)
				room.performPlayerAction(playerAction)
		}
		// update focused room last (because items could have been picked up in other rooms)
		rooms[roomFocus.currentFocusIndex].performPlayerAction(playerAction)
		if (done) {
			Log.info("level '$levelName', stage $numberOfShownRooms done")
		} else if (playerStatus.isDead) {
			Log.info("You died!")
		}
	}
	
	fun render() {
		for (roomIndex in 0..rooms.size - 1) {
			val offset = computeOffsetForRoom(roomIndex)
			rooms[roomIndex].render(offset)
		}
		
		val statusCorner = Hud.upperLeft
		statusCorner.clear()
		statusCorner.addLine(playerStatus.toString())
		for (line in PlayerInventory.toString().lines())
			statusCorner.addLine(line)
	}
	
	private fun computeOffsetForRoom(roomIndex: Int): Coordi {
		val (x, y) = roomFocus.indexToCoord(roomIndex)
		val xOffset = 0.seq(x - 1).map { rooms[RoomFocus.coordToIndex(Coordi(it, y))].width + 1 }.sum()
		val yOffset = 0.seq(y - 1).map { b ->
			0.seq(RoomFocus.metaLayoutWidth - 1).
					map { rooms[RoomFocus.coordToIndex(Coordi(it, b))].height }.max()!! + 1
		}.sum() + rooms[roomIndex].height
		return Coordi(xOffset, -yOffset)
	}
	
	
	val centerCoordinates: Vector2
		get() = Vector2(totalWidth, -totalHeight) * 0.5
	
}