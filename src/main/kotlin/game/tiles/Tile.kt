package game.tiles

import game.GfxUtil
import game.entities.Entity
import gl.gfx.texture.Texture
import gl.gfx.texture.TextureManager
import math.linearalgebra.Coordi
import util.astLess
import java.util.*

abstract class Tile(var coordinates: Coordi) {
	
	companion object {
		val defaultZValue: Double = 0.0
	}
	
	abstract fun canPlayerEnterTile(isRoomFocused: Boolean): Boolean
	
	abstract fun renderFloorTile(isRoomFocused: Boolean)
	
	fun renderRestOfTile(isRoomFocused: Boolean) {
		renderExtraGraphics()
	}
	
	protected fun renderQuad(texture: Texture) {
		GfxUtil.renderQuad(coordinates, texture, defaultZValue)
	}
	
	protected val additionalTextureNames = ArrayList<String>()
	
	fun addAdditionalGroundTextureName(newTextureName: String) {
		additionalTextureNames.add(newTextureName)
	}
	
	fun removeAdditionalGroundTextureName(newTextureName: String) {
		additionalTextureNames.remove(newTextureName)
	}
	
	private fun renderExtraGraphics() {
		astLess(additionalTextureNames.size, 10)
		astLess(defaultZValue, Entity.defaultZValue)
		var z = defaultZValue
		for (textureName in additionalTextureNames) {
			z += (Entity.defaultZValue - defaultZValue) / 10
			GfxUtil.renderQuad(coordinates, TextureManager.instance[textureName],
					z = z)
		}
	}
	
}

