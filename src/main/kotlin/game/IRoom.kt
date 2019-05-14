package game

import game.entities.Entity
import game.entities.Player
import game.tiles.Tile
import math.linearalgebra.Coordi

interface IRoom {
	
	data class Quad(val tile: Tile, val entity: Entity)
	
	val width: Int
	val height: Int
	val isFocused: Boolean
	val player: Player
	val playerStatus: PlayerStatus
	operator fun get(coordi: Coordi): Quad
	operator fun contains(coordi: Coordi): Boolean
	
	fun updateEntityAt(coordi: Coordi, newEntity: Entity)
	fun addAdditionalGroundTextureName(coordi: Coordi, extraTextureName: String)
	fun changePlayerStatus(newPlayerStatus: PlayerStatus)
	fun addItemToInventory(newItem: PlayerInventory.Item)
	fun removeItemFromInventory(newItem: PlayerInventory.Item)
	fun updatePlayerCoordinates(newCoordinates: Coordi)
	
	
}