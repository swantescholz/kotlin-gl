package game.entities

import game.IRoom
import game.PlayerInventory
import math.linearalgebra.Coordi
import util.astTrue

class Door(coordinates: Coordi) : Entity(coordinates) {
	override fun canPlayerEnterField(room: IRoom): Boolean {
		return room.isFocused && PlayerInventory.hasItem(PlayerInventory.Item.Key)
	}
	
	override fun reactToPlayerEntering(room: IRoom) {
		astTrue(PlayerInventory.hasItem(PlayerInventory.Item.Key))
		room.removeItemFromInventory(PlayerInventory.Item.Key)
		room.addAdditionalGroundTextureName(coordinates, "pcute/door-tall-open")
	}
	
	override fun render(room: IRoom) {
		renderQuad("pcute/door-tall-closed")
	}
	
}