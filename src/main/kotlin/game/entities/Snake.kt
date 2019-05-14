package game.entities

import game.IRoom
import game.PlayerInventory
import math.linearalgebra.Coordi
import util.Config
import util.extensions.int

class Snake(coordinates: Coordi) : Entity(coordinates) {
	override fun reactToPlayerEntering(room: IRoom) {
		if (room.isFocused && PlayerInventory.hasItem(PlayerInventory.Item.Sword)) {
			room.removeItemFromInventory(PlayerInventory.Item.Sword)
		} else {
			val status = room.playerStatus
			val newStatus = status.copy(vitality = status.vitality - Config.game["snake.minus.vitality"].int)
			room.changePlayerStatus(newStatus)
		}
	}
	
	override fun canPlayerEnterField(room: IRoom): Boolean = true
	
	override fun render(room: IRoom) {
		renderQuad("snake")
	}
}