package gl.gfx.model

import gl.math.Triangle
import math.linearalgebra.Vector2
import math.linearalgebra.Vector3
import java.util.*

class IndexedTriangle {
	var a = IndexedVertex()
	var b = IndexedVertex()
	var c = IndexedVertex()
	
	fun createTriangle(positions: ArrayList<Vector3>, normals: ArrayList<Vector3>, texCoords: ArrayList<Vector2>): Triangle {
		val triangle = Triangle()
		triangle.a.position = positions[a.positionIndex]
		triangle.b.position = positions[b.positionIndex]
		triangle.c.position = positions[c.positionIndex]
		triangle.a.normal = normals[a.normalIndex]
		triangle.b.normal = normals[b.normalIndex]
		triangle.c.normal = normals[c.normalIndex]
		if (a.hasTexCoords() && b.hasTexCoords() && c.hasTexCoords()) {
			triangle.a.texCoord = texCoords[a.texCoordIndex]
			triangle.b.texCoord = texCoords[b.texCoordIndex]
			triangle.c.texCoord = texCoords[c.texCoordIndex]
		}
		return triangle
	}
	
	fun computeNormal(positions: ArrayList<Vector3>): Vector3 {
		val va = positions[a.positionIndex]
		val vb = positions[b.positionIndex]
		val vc = positions[c.positionIndex]
		return Triangle.normal(va, vb, vc)
	}
	
	fun setEveryNormalIndex(normalIndex: Int) {
		a.normalIndex = normalIndex
		b.normalIndex = normalIndex
		c.normalIndex = normalIndex
	}
	
	fun setPositionIndices(index1: Int, index2: Int, index3: Int) {
		a.positionIndex = index1
		b.positionIndex = index2
		c.positionIndex = index3
	}
	
	fun setNormalIndices(normal1: Int, normal2: Int, normal3: Int) {
		a.normalIndex = normal1
		b.normalIndex = normal2
		c.normalIndex = normal3
	}
	
	fun setTexCoordIndices(texCoord1: Int, texCoord2: Int, texCoord3: Int) {
		a.texCoordIndex = texCoord1
		b.texCoordIndex = texCoord2
		c.texCoordIndex = texCoord3
	}
}
