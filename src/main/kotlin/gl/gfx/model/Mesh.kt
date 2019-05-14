package gl.gfx.model

import gl.math.Triangle
import gl.util.StringUtil
import math.linearalgebra.Vector2
import math.linearalgebra.Vector3
import util.NotImplementedException
import util.log.Log
import java.util.*

class Mesh : Renderable {
	
	val positions = ArrayList<Vector3>()
	val normals = ArrayList<Vector3>()
	val texCoords = ArrayList<Vector2>()
	val indexedTriangles: MutableList<IndexedTriangle> = ArrayList()
	
	private val vbo = Vbo()
	
	fun createVbo() {
		val triangles = createTriangles()
		vbo.create(triangles)
	}
	
	fun createTriangles(): List<Triangle> {
		val triangles = ArrayList<Triangle>()
		for (indexedTriangle in indexedTriangles) {
			val triangle = indexedTriangle.createTriangle(positions, normals, texCoords)
			triangles.add(triangle)
		}
		return triangles
	}
	
	override fun render() {
		vbo.render()
	}
	
	private fun readObjLine(line: String) {
		if (line.startsWith("vn")) {
			readObjNormal(line)
		} else if (line.startsWith("vt")) {
			readObjTexCoord(line)
		} else if (line.startsWith("v")) {
			readObjPosition(line)
		} else if (line.startsWith("f")) {
			readObjFace(line)
		} else {
			Log.errorAndThrow(RuntimeException("bad file! line '$line' cannot be read as OBJ line"))
		}
	}
	
	private fun readObjFace(line: String) {
		val tokens = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		if (line.contains("//"))
			readObjFacePositionNormal(tokens)
		else if (StringUtil.countSubstring(line, "/") / (tokens.size - 1) == 2)
			readObjFacePositionTextureNormal(tokens)
		else if (line.contains("/"))
			readObjFacePositionTexture(tokens)
		else
			readObjFacePosition(tokens)
	}
	
	private fun readObjIndex(str: String): Int {
		return Integer.parseInt(str) - 1
	}
	
	private fun readObjFacePosition(tokens: Array<String>) {
		val index1 = readObjIndex(tokens[1])
		for (i in 2..tokens.size - 1 - 1) {
			val index2 = readObjIndex(tokens[i])
			val index3 = readObjIndex(tokens[i + 1])
			addTriangleWithPlaneNormal(index1, index2, index3)
		}
	}
	
	private fun readObjFacePositionTexture(tokens: Array<String>) {
		throw NotImplementedException()
	}
	
	private fun readObjFacePositionTextureNormal(tokens: Array<String>) {
		val subtokens1 = tokens[1].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		val index1 = readObjIndex(subtokens1[0])
		val texCoord1 = readObjIndex(subtokens1[1])
		val normal1 = readObjIndex(subtokens1[2])
		for (i in 2..tokens.size - 1 - 1) {
			val subtokens2 = tokens[i].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
			val subtokens3 = tokens[i + 1].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
			
			val index2 = readObjIndex(subtokens2[0])
			val index3 = readObjIndex(subtokens3[0])
			val texCoord2 = readObjIndex(subtokens2[1])
			val texCoord3 = readObjIndex(subtokens3[1])
			val normal2 = readObjIndex(subtokens2[2])
			val normal3 = readObjIndex(subtokens3[2])
			addTriangleWithTexCoordsAndNormals(index1, index2, index3,
					texCoord1, texCoord2, texCoord3,
					normal1, normal2, normal3)
		}
	}
	
	private fun readObjFacePositionNormal(tokens: Array<String>) {
		val subtokens1 = tokens[1].split("//".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		val index1 = readObjIndex(subtokens1[0])
		val normal1 = readObjIndex(subtokens1[1])
		for (i in 2..tokens.size - 1 - 1) {
			val subtokens2 = tokens[i].split("//".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
			val subtokens3 = tokens[i + 1].split("//".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
			
			val index2 = readObjIndex(subtokens2[0])
			val index3 = readObjIndex(subtokens3[0])
			val normal2 = readObjIndex(subtokens2[1])
			val normal3 = readObjIndex(subtokens3[1])
			addTriangleWithNormals(index1, index2, index3, normal1, normal2, normal3)
		}
	}
	
	private fun addTriangleWithNormals(index1: Int, index2: Int, index3: Int,
	                                   normal1: Int, normal2: Int, normal3: Int) {
		val indexedTriangle = IndexedTriangle()
		indexedTriangle.setPositionIndices(index1, index2, index3)
		indexedTriangle.setNormalIndices(normal1, normal2, normal3)
		indexedTriangles.add(indexedTriangle)
	}
	
	private fun addTriangleWithTexCoordsAndNormals(index1: Int, index2: Int, index3: Int,
	                                               texCoord1: Int, texCoord2: Int, texCoord3: Int,
	                                               normal1: Int, normal2: Int, normal3: Int) {
		val indexedTriangle = IndexedTriangle()
		indexedTriangle.setPositionIndices(index1, index2, index3)
		indexedTriangle.setTexCoordIndices(texCoord1, texCoord2, texCoord3)
		indexedTriangle.setNormalIndices(normal1, normal2, normal3)
		indexedTriangles.add(indexedTriangle)
	}
	
	private fun readObjPosition(line: String) {
		positions.add(readObjVector3(line))
	}
	
	private fun readObjNormal(line: String) {
		normals.add(readObjVector3(line))
	}
	
	private fun readObjTexCoord(line: String) {
		texCoords.add(readObjVector2(line))
	}
	
	private fun readObjVector2(line: String): Vector2 {
		val tokens = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		val x = java.lang.Double.parseDouble(tokens[1])
		val y = java.lang.Double.parseDouble(tokens[2])
		return Vector2(x, y)
	}
	
	private fun readObjVector3(line: String): Vector3 {
		val tokens = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		val x = java.lang.Double.parseDouble(tokens[1])
		val y = java.lang.Double.parseDouble(tokens[2])
		val z = java.lang.Double.parseDouble(tokens[3])
		return Vector3(x, y, z)
	}
	
	private fun readOffFaceTriangles(indexTokens: Array<String>) {
		val indexCount = Integer.parseInt(indexTokens[0])
		val index1 = Integer.parseInt(indexTokens[1])
		var i = 1
		while (i + 1 < indexCount) {
			val index2 = Integer.parseInt(indexTokens[i + 1])
			val index3 = Integer.parseInt(indexTokens[i + 2])
			
			addTriangleWithPlaneNormal(index1, index2, index3)
			i++
		}
	}
	
	private fun addTriangleWithPlaneNormal(index1: Int, index2: Int, index3: Int) {
		val a = positions[index1]
		val b = positions[index2]
		val c = positions[index3]
		val normal = -Triangle.normal(a, b, c)
		normals.add(normal)
		val normalIndex = normals.size - 1
		val indexedTriangle = IndexedTriangle()
		indexedTriangle.setPositionIndices(index1, index2, index3)
		indexedTriangle.setEveryNormalIndex(normalIndex)
		indexedTriangles.add(indexedTriangle)
	}
	
	private fun readOffVertex(coords: Array<String>) {
		val x = java.lang.Double.parseDouble(coords[0])
		val y = java.lang.Double.parseDouble(coords[1])
		val z = java.lang.Double.parseDouble(coords[2])
		positions.add(Vector3(x, y, z))
	}
	
	companion object {
		
		fun createFromObj(obj: String): Mesh {
			val mesh = Mesh()
			val lines = ArrayList(Arrays.asList(*obj.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
			for (i in lines.indices) {
				var line = lines[i]
				line = line.trim { it <= ' ' }
				lines[i] = line
			}
			for (line in lines) {
				if (line.startsWith("#"))
					continue
				mesh.readObjLine(line)
			}
			return mesh
		}
		
		fun createFromOff(off: String): Mesh {
			val mesh = Mesh()
			val lines = off.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
			val info = lines[1]
			val tokens = info.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
			val vertexCount = Integer.parseInt(tokens[0])
			val faceCount = Integer.parseInt(tokens[1])
			val vertexLines = ArrayList<String>()
			val faceLines = ArrayList<String>()
			vertexLines.addAll(Arrays.asList(*lines).subList(2, vertexCount + 2))
			for (i in 0..faceCount - 1) {
				faceLines.add(lines[i + vertexCount + 2])
			}
			
			for (line in vertexLines) {
				val coords = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				mesh.readOffVertex(coords)
			}
			for (line in faceLines) {
				val indexTokens = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				mesh.readOffFaceTriangles(indexTokens)
			}
			return mesh
		}
	}
	
}
