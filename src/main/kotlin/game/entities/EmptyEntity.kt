package game.entities

import game.IRoom
import math.linearalgebra.Coordi

class EmptyEntity(coordinates: Coordi) : Entity(coordinates) {
	override fun render(room: IRoom) = Unit
	override fun canNpcEnterField(): Boolean = true
	override fun canPlayerEnterField(room: IRoom) = true
}