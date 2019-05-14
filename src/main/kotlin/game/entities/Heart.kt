package game.entities

import game.IRoom
import math.linearalgebra.Coordi
import util.Config
import util.extensions.int

class Heart(coordinates: Coordi) : Entity(coordinates) {
	override fun reactToPlayerEntering(room: IRoom) {
		val status = room.playerStatus
		val newStatus = status.copy(vitality = status.vitality + Config.game["heart.plus.vitality"].int)
		room.changePlayerStatus(newStatus)
	}
	
	override fun render(room: IRoom) {
		renderQuad("pcute/heart")
	}
}