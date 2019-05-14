package gl.gfx.model

import gl.gfx.gl
import gl.math.Triangle
import gl.math.Vertex
import java.nio.DoubleBuffer
import java.nio.IntBuffer
import java.util.*
import javax.media.opengl.GL

class Vbo {
	
	
	private var totalNumVerts: Int = 0
	private var id = 0
	
	private var stride: Int = 0
	private var vertexPointer: Int = 0
	private var normalPointer: Int = 0
	private var texCoordPointer = 0
	private var hasTexCoords = true
	
	fun create(triangles: List<Triangle>) {
		val gl = gl()
		hasTexCoords = true
		
		val vertices = ArrayList<Vertex>()
		for (triangle in triangles) {
			if (!triangle.hasTexCoords()) {
				hasTexCoords = false
			}
			vertices.add(triangle.a)
			vertices.add(triangle.b)
			vertices.add(triangle.c)
		}
		
		totalNumVerts = vertices.size
		
		val buf = IntBuffer.allocate(1)
		gl.glGenBuffers(1, buf)
		id = buf.get()
		
		var doublesPerVert = 6
		if (hasTexCoords)
			doublesPerVert += 2
		
		val data = DoubleBuffer.allocate(totalNumVerts * doublesPerVert)
		for (vertex in vertices) {
			if (hasTexCoords)
				data.put(vertex.texCoord!!.data())
			data.put(vertex.normal.data())
			data.put(vertex.position.data())
		}
		data.rewind()
		
		val bytesPerDouble = java.lang.Double.SIZE / java.lang.Byte.SIZE
		
		val numBytes = data.capacity() * bytesPerDouble
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id)
		gl.glBufferData(GL.GL_ARRAY_BUFFER, numBytes, data, GL.GL_STATIC_DRAW)
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)
		
		if (!hasTexCoords) {
			texCoordPointer = -2
		}
		normalPointer = 2 + texCoordPointer
		vertexPointer = 3 + normalPointer
		stride = 3 + vertexPointer
		texCoordPointer *= bytesPerDouble
		normalPointer *= bytesPerDouble
		vertexPointer *= bytesPerDouble
		stride *= bytesPerDouble
	}
	
	fun render() {
		val gl = gl()
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id)
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
		gl.glEnableClientState(GL.GL_NORMAL_ARRAY)
		if (hasTexCoords) {
			gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
			gl.glTexCoordPointer(2, GL.GL_DOUBLE, stride, texCoordPointer.toLong())
		}
		gl.glNormalPointer(GL.GL_DOUBLE, stride, normalPointer.toLong())
		gl.glVertexPointer(3, GL.GL_DOUBLE, stride, vertexPointer.toLong())
		
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, totalNumVerts)
		
		gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
		gl.glDisableClientState(GL.GL_NORMAL_ARRAY)
		if (hasTexCoords) {
			gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
		}
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)
	}
	
	fun dispose() {
		if (id != 0) {
			gl().glDeleteBuffers(1, intArrayOf(id), 0)
			id = 0
		}
	}
	
	override fun toString(): String {
		return "Vertex Buffer Object (VBO): " + id
	}
	
}
