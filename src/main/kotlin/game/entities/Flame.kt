package game.entities

import game.IRoom
import math.linearalgebra.Coordi
import util.Config
import util.extensions.int

class Flame(coordinates: Coordi) : Entity(coordinates) {
	
	override fun reactToPlayerEntering(room: IRoom) {
		val status = room.playerStatus
		val newStatus = status.copy(vitality = status.vitality - Config.game["dragon.flame.minus.vitality"].int)
		room.changePlayerStatus(newStatus)
	}
	
	private var aliveStepsCounter = 0
	override fun performStepAfterPlayerStepped(room: IRoom) {
		aliveStepsCounter++
		if (aliveStepsCounter >= Config.game["dragon.flame.lifespan"].int) {
			aliveStepsCounter = 0
			room.updateEntityAt(coordinates, EmptyEntity(coordinates))
		}
	}
	
	override fun render(room: IRoom) {
		renderQuad("lava")
	}
}