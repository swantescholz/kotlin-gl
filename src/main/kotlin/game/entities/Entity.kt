package game.entities

import game.Direction
import game.GfxUtil
import game.IRoom
import gl.gfx.texture.TextureManager
import math.linearalgebra.Coordi
import math.linearalgebra.Vector2
import util.extensions.int
import util.extensions.string
import util.log.Log

abstract class Entity(var coordinates: Coordi) {
	
	abstract fun render(room: IRoom)
	open fun canPlayerEnterField(room: IRoom): Boolean = true
	open fun canNpcEnterField(): Boolean = false
	
	val realCenterCoordinates: Vector2
		get() = Vector2(coordinates.x + 0.5, coordinates.y + 0.5)
	
	open fun performStepAfterPlayerStepped(room: IRoom) = Unit
	open fun reactToPlayerEntering(room: IRoom) = Unit
	
	protected fun renderQuad(textureName: String, z: Double = defaultZValue, cwRotations: Int = 0) {
		GfxUtil.renderQuad(coordinates, TextureManager.instance[textureName], z = z, cwRotations = cwRotations)
	}
	
	companion object {
		val defaultZValue: Double = 0.01
		fun fromChar(coordi: Coordi, character: Char): Entity {
			return when (character) {
				'.', 'X', 'x' -> EmptyEntity(coordi)
				'p' -> Player(coordi)
				'h' -> Heart(coordi)
				'd' -> Door(coordi)
				'k' -> Key(coordi)
				'c' -> Coin(coordi)
				's' -> Sword(coordi)
				'n' -> Snake(coordi)
				'0', '1', '2', '3' -> Dragon(coordi, Direction.values()[character.string.int])
				else -> {
					Log.errorAndThrow("bad character for level loading: '$character'")
					null!!
				}
			}
		}
	}
}

