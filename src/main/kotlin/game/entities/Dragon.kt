package game.entities

import game.Direction
import game.IRoom
import game.PlayerInventory
import math.linearalgebra.Coordi
import util.Config
import util.astTrue
import util.extensions.int

class Dragon(coordinates: Coordi, val faceDirection: Direction) : Entity(coordinates) {
	override fun reactToPlayerEntering(room: IRoom) {
		astTrue(room.isFocused && PlayerInventory.hasItem(PlayerInventory.Item.Sword))
		PlayerInventory.removeItem(PlayerInventory.Item.Sword)
	}
	
	private var stepCounter = 0
	override fun performStepAfterPlayerStepped(room: IRoom) {
		fun spewFlames() {
			var flameCoords = coordinates
			while (true) {
				flameCoords += faceDirection.offset
				if (flameCoords !in room)
					break
				val (tile, entity) = room[flameCoords]
				if (entity.canNpcEnterField()) {
					val flameEntity = Flame(flameCoords)
					if (flameCoords == room.player.coordinates) {
						flameEntity.reactToPlayerEntering(room)
						break
					}
					room.updateEntityAt(flameCoords, flameEntity)
				} else {
					break
				}
			}
		}
		stepCounter++
		if (stepCounter >= Config.game["dragon.spew.flame.steps"].int) {
			stepCounter = 0
			spewFlames()
		}
	}
	
	
	override fun canPlayerEnterField(room: IRoom): Boolean {
		return room.isFocused && PlayerInventory.hasItem(PlayerInventory.Item.Sword)
	}
	
	override fun render(room: IRoom) {
		renderQuad("dragonb", cwRotations = (faceDirection.ordinal + 2) % 4)
	}
}