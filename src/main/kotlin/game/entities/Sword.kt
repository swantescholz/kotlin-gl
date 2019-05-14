package game.entities

import game.IRoom
import game.PlayerInventory
import math.linearalgebra.Coordi

class Sword(coordinates: Coordi) : Entity(coordinates) {
	override fun reactToPlayerEntering(room: IRoom) {
		room.addItemToInventory(PlayerInventory.Item.Sword)
	}
	
	override fun render(room: IRoom) {
		renderQuad("sword")
	}
}