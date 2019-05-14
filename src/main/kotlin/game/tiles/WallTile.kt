package game.tiles

import gl.gfx.texture.Texture
import gl.gfx.texture.TextureManager
import math.linearalgebra.Coordi

class WallTile(coordinates: Coordi) : Tile(coordinates) {
	companion object {
		val basicTexture: Texture = TextureManager.instance["ice"]
	}
	
	override fun canPlayerEnterTile(isRoomFocused: Boolean): Boolean {
		return false
	}
	
	override fun renderFloorTile(isRoomFocused: Boolean) {
		renderQuad(basicTexture)
	}
}
