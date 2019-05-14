package gl.gfx.model

class IndexedVertex {
	var positionIndex = -1
	var texCoordIndex = -1
	var normalIndex = -1
	
	fun hasTexCoords(): Boolean {
		return texCoordIndex >= 0
	}
}
