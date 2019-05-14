package game.entities

import game.IRoom
import game.PlayerAction
import math.linearalgebra.Coordi
import util.astIn
import java.util.*

class EntityMap {
	private val map = TreeMap<Coordi, Entity>()
	
	fun performPlayerAction(playerAction: PlayerAction, room: IRoom) {
		room.player.performAction(playerAction, room)
		for (entity in map.values) {
			entity.performStepAfterPlayerStepped(room)
		}
	}
	
	
	operator fun get(coordi: Coordi): Entity {
		astIn(coordi, map.keys)
		return map[coordi]!!
	}
	
	operator fun contains(coordi: Coordi) = coordi in map
	
	operator fun set(coordi: Coordi, entity: Entity) {
		map[coordi] = entity
	}
	
	fun render(room: IRoom) {
		for (entity in map.values) {
			entity.render(room)
		}
	}
	
	fun clear() {
		map.clear()
	}
}