package game

import game.entities.EmptyEntity
import game.entities.Entity
import game.entities.EntityMap
import game.entities.Player
import game.tiles.*
import gl.gfx.GlUtil
import gl.gfx.gl
import gl.gfx.shader.Program
import gl.gfx.shader.ProgramManager
import gl.gfx.shader.uniform.UniformManager
import gl.math.Material
import math.linearalgebra.Coordi
import math.linearalgebra.Matrix
import math.linearalgebra.Vector2
import math.linearalgebra.Vector3
import util.astNull
import util.astTrue
import util.extensions.double
import util.extensions.toal
import util.log.Log
import javax.media.opengl.GL

class Room(val roomIndex: Int, val roomFocus: RoomFocus, val undoManager: UndoManager,
           override val playerStatus: PlayerStatus) : IRoom {
	override fun updatePlayerCoordinates(newCoordinates: Coordi) {
		val oldCoordinates = player.coordinates
		undoManager.addActionToGroup({
			player.coordinates = newCoordinates
		}, {
			player.coordinates = oldCoordinates
		})
	}
	
	override fun get(coordi: Coordi): IRoom.Quad {
		return IRoom.Quad(tileMap[coordi], entityMap[coordi])
	}
	
	override fun contains(coordi: Coordi): Boolean = coordi in tileMap
	
	override fun updateEntityAt(coordi: Coordi, newEntity: Entity) {
		val oldEntity = entityMap[coordi]
		undoManager.addActionToGroup({
			entityMap[coordi] = newEntity
		}, {
			entityMap[coordi] = oldEntity
		})
		
	}
	
	override fun addAdditionalGroundTextureName(coordi: Coordi, extraTextureName: String) {
		val tile = tileMap[coordi]
		undoManager.addActionToGroup({
			tile.addAdditionalGroundTextureName(extraTextureName)
		}, {
			tile.removeAdditionalGroundTextureName(extraTextureName)
		})
	}
	
	override fun changePlayerStatus(newPlayerStatus: PlayerStatus) {
		val oldPlayerStatus = playerStatus.copy()
		undoManager.addActionToGroup({
			playerStatus.setTo(newPlayerStatus)
		}, {
			playerStatus.setTo(oldPlayerStatus)
		})
	}
	
	override fun addItemToInventory(newItem: PlayerInventory.Item) {
		undoManager.addActionToGroup({
			PlayerInventory.addItem(newItem)
		}, {
			PlayerInventory.removeItem(newItem)
		})
	}
	
	override fun removeItemFromInventory(newItem: PlayerInventory.Item) {
		undoManager.addActionToGroup({
			PlayerInventory.removeItem(newItem)
		}, {
			PlayerInventory.addItem(newItem)
			PlayerInventory.startNewStep()
		})
	}
	
	private val tileMap = TileMap()
	private val entityMap = EntityMap()
	
	var done: Boolean = false
		private set
	private var myPlayer: Player? = null
	override val player: Player
		get() = myPlayer!!
	
	fun hasPlayer() = myPlayer != null
	override val height: Int
		get() = tileMap.height
	override val width: Int
		get() = tileMap.width
	val roomName: String
		get() = RoomFocus.getNameForRoomIndex(roomIndex)
	override val isFocused: Boolean
		get() = roomIndex == roomFocus.currentFocusIndex
	
	fun loadFromString(roomAsString: String) {
		fun computeTileAndEntityFromChar(character: Char, coordi: Coordi): Pair<Tile, Entity> {
			val c = character.toLowerCase()
			return when (c) {
				'x' -> Pair(WallTile(coordi), EmptyEntity(coordi))
				'g' -> Pair(GoalTile(coordi), EmptyEntity(coordi))
				else -> Pair(SimpleTile(coordi), Entity.fromChar(coordi, character))
			}
		}
		tileMap.clear()
		entityMap.clear()
		val lines = roomAsString.lines().filter { it.length > 0 }.toal()
		for (y in 0..lines.size - 1) {
			val line = lines[lines.size - 1 - y]
			for (x in 0..line.length - 1) {
				val coordi = Coordi(x, y)
				val char = line[x]
				if (char == ' ') {
					continue
				}
				val (tile, entity) = computeTileAndEntityFromChar(char, coordi)
				tileMap[coordi] = tile
				entityMap[coordi] = entity
				if (entity is Player) {
					astNull(myPlayer)
					myPlayer = entity
				}
			}
		}
		astTrue(hasPlayer(), "no player in room's '$roomName' input:\n$roomAsString")
	}
	
	
	fun performPlayerAction(playerAction: PlayerAction) {
		if (!done) {
			entityMap.performPlayerAction(playerAction, this as IRoom)
			if (tileMap[player.coordinates] is GoalTile) {
				undoManager.addActionToGroup({
					done = true
					Log.info("room '$roomName' done!")
				}, {
					done = false
				})
			}
		}
	}
	
	fun render(lowerLeftOffset: Coordi) {
		fun renderFocusBorder() {
			Material.FOREST.use()
			ProgramManager.instance["color"].use()
			gl().glBegin(GL.GL_LINE_STRIP)
			val borderOffset = 0.1
			GlUtil.direct(Vector2(-borderOffset, -borderOffset))
			GlUtil.direct(Vector2(width + borderOffset, -borderOffset))
			GlUtil.direct(Vector2(width + borderOffset, height + borderOffset))
			GlUtil.direct(Vector2(-borderOffset, height + borderOffset))
			GlUtil.direct(Vector2(-borderOffset, -borderOffset))
			gl().glEnd()
		}
		Program.push()
		val oldModelMatrix = UniformManager.getModelMatrix()
		UniformManager.setModelMatrix(Matrix.translation(Vector3(
				lowerLeftOffset.x.double, lowerLeftOffset.y.double, 0.0)))
		if (isFocused) {
			renderFocusBorder()
		}
		ProgramManager.instance["texture"].use()
		tileMap.render(this as IRoom)
		entityMap.render(this as IRoom)
		UniformManager.setModelMatrix(oldModelMatrix)
		Program.pop()
	}
	
	fun computeRealFocusedPlayerPosition(lowerLeftOffset: Coordi): Vector2 {
		return player.realCenterCoordinates + lowerLeftOffset.vec2
	}
	
}

