package game.tiles

import gl.gfx.texture.Texture
import gl.gfx.texture.TextureManager
import math.linearalgebra.Coordi

class SimpleTile(coordinates: Coordi) : Tile(coordinates) {
	
	
	companion object {
		val basicTexture: Texture = TextureManager.instance["pcute/wood-block"]
	}
	
	override fun canPlayerEnterTile(isRoomFocused: Boolean): Boolean = true
	
	override fun renderFloorTile(isRoomFocused: Boolean) {
		renderQuad(basicTexture)
	}
	
	
}
