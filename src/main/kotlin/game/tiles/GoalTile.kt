package game.tiles

import gl.gfx.texture.Texture
import math.linearalgebra.Coordi

class GoalTile(coordinates: Coordi) : Tile(coordinates) {
	companion object {
		val basicTexture: Texture = SimpleTile.basicTexture
	}
	
	init {
		addAdditionalGroundTextureName("pcute/selector")
	}
	
	override fun canPlayerEnterTile(isRoomFocused: Boolean): Boolean {
		return true
	}
	
	override fun renderFloorTile(isRoomFocused: Boolean) {
		renderQuad(basicTexture)
	}

}
