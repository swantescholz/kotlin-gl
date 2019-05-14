package game.entities

import game.IRoom
import game.PlayerInventory
import math.linearalgebra.Coordi

class Key(coordi: Coordi) : Entity(coordi) {
	override fun render(room: IRoom) {
		renderQuad("pcute/key")
	}
	
	override fun reactToPlayerEntering(room: IRoom) {
		room.addItemToInventory(PlayerInventory.Item.Key)
	}
	
}