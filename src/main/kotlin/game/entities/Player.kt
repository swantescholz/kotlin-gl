package game.entities

import game.Direction
import game.IRoom
import game.PlayerAction
import math.linearalgebra.Coordi
import util.astFail

class Player(coordinates: Coordi) : Entity(coordinates) {
	
	override fun canPlayerEnterField(room: IRoom) = true
	override fun canNpcEnterField() = true
	
	override fun reactToPlayerEntering(room: IRoom) {
		astFail("player should never enter player")
	}
	
	override fun render(room: IRoom) {
		if (room.isFocused) {
			renderQuad("pcute/selector", z = playerZValue - 0.01)
		}
		if (room.playerStatus.isDead) {
			renderQuad("kill", z = playerZValue)
		} else {
			renderQuad("pcute/character-horn-girl", z = playerZValue)
		}
	}
	
	fun performAction(playerAction: PlayerAction, room: IRoom) {
		fun move(direction: Direction) {
			val destination = coordinates + direction.offset
			if (destination !in room) {
				return
			}
			val (otherTile, otherEntity) = room[destination]
			if (otherTile.canPlayerEnterTile(room.isFocused) && otherEntity.canPlayerEnterField(room)) {
				room.updateEntityAt(coordinates, newEntity = EmptyEntity(coordinates))
				room.updatePlayerCoordinates(newCoordinates = destination)
				otherEntity.reactToPlayerEntering(room)
				room.updateEntityAt(destination, newEntity = this)
			}
		}
		when (playerAction) {
			PlayerAction.UP -> move(Direction.UP)
			PlayerAction.RIGHT -> move(Direction.RIGHT)
			PlayerAction.DOWN -> move(Direction.DOWN)
			PlayerAction.LEFT -> move(Direction.LEFT)
		}
	}
	
	companion object {
		val playerZValue = Entity.defaultZValue + 0.01
	}
}