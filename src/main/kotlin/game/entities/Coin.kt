package game.entities

import game.IRoom
import math.linearalgebra.Coordi
import util.Config
import util.extensions.int

class Coin(coordinates: Coordi) : Entity(coordinates) {
	override fun reactToPlayerEntering(room: IRoom) {
		val status = room.playerStatus
		val newStatus = status.copy(finance = status.finance + Config.game["coin.plus.finance"].int)
		room.changePlayerStatus(newStatus)
	}
	
	override fun render(room: IRoom) {
		renderQuad("pcute/gem-green")
	}
}