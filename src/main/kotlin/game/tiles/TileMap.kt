package game.tiles

import game.IRoom
import math.IMAX
import math.IMIN
import math.linearalgebra.Coordi
import util.astIn
import java.util.*

class TileMap {
	private val map = TreeMap<Coordi, Tile>()
	
	private var minCoords = Coordi(IMAX, IMAX)
	private var maxCoords = Coordi(IMIN, IMIN)
	
	val width: Int
		get() = maxCoords.x - minCoords.x + 1
	val height: Int
		get() = maxCoords.y - minCoords.y + 1
	
	operator fun get(coordi: Coordi): Tile {
		astIn(coordi, map.keys)
		return map[coordi]!!
	}
	
	operator fun contains(coordi: Coordi) = coordi in map
	
	operator fun set(coordi: Coordi, tile: Tile) {
		map[coordi] = tile
		minCoords = minCoords.min(coordi)
		maxCoords = maxCoords.max(coordi)
	}
	
	fun render(room: IRoom) {
		for (tile in map.values) {
			tile.renderFloorTile(room.isFocused)
		}
		for (tile in map.values) {
			tile.renderRestOfTile(room.isFocused)
		}
	}
	
	fun clear() {
		map.clear()
	}
}