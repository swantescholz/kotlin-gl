package gl.gfx.model

import gl.gfx.GlUtil
import gl.gfx.gl
import gl.math.Material
import gl.math.Triangle
import math.linearalgebra.MathUtil
import math.linearalgebra.Vector2
import math.linearalgebra.Vector3
import util.datastructures.Array2
import java.util.*
import javax.media.opengl.GL

class Grid(val width: Int, val height: Int) {
	
	fun renderControlNodes(obj: RenderableObject) {
		val oldPosition = obj.position
		for (y in 0..height - 1) {
			for (x in 0..width - 1) {
				val p = getNode(x, y).position
				obj.position = p
				obj.render()
			}
		}
		obj.position = oldPosition
	}
	
	inner class Node {
		var position = Vector3()
		var normal = Vector3()
	}
	
	private val data: Array2<Node>
	
	init {
		data = Array2(width, height, { x, y -> Node() })
	}
	
	fun arrangePlane(upperLeft: Vector3, upperRight: Vector3, lowerLeft: Vector3) {
		val lowerRight = lowerLeft + upperRight - upperLeft
		for (y in 0..height - 1) {
			for (x in 0..width - 1) {
				val node = data.get(x, y)
				val fxy = getFactors(x, y)
				node.position = upperLeft.interpolateBilinear(upperRight, lowerLeft, lowerRight,
						fxy.x, fxy.y)
			}
		}
	}
	
	fun randomizeAxis(axis: Vector3, minAmount: Double, maxAmount: Double) {
		for (y in 0..height - 1) {
			for (x in 0..width - 1) {
				val node = getNode(x, y)
				node.position = node.position + (axis * (MathUtil.randomDouble(minAmount, maxAmount)))
			}
		}
	}
	
	private fun getFactors(x: Int, y: Int): Vector2 {
		return Vector2(x.toDouble() / (width - 1), y.toDouble() / (height - 1))
	}
	
	fun createTriangles(smoothNormals: Boolean): List<Triangle> {
		computeSmoothNormals()
		val triangles = ArrayList<Triangle>()
		for (y in 0..height - 1 - 1) {
			addTrianglesOfRow(triangles, y, smoothNormals)
		}
		return triangles
	}
	
	private fun computeSmoothNormals() {
		for (y in 0..height - 1) {
			for (x in 0..width - 1) {
				val node = getNode(x, y)
				var up: Vector3? = null
				var dn: Vector3? = null
				var rt: Vector3? = null
				var lt: Vector3? = null
				if (y > 0)
					up = getNode(x, y - 1).position - node.position
				if (x + 1 < width)
					rt = getNode(x + 1, y).position - node.position
				if (y + 1 < height)
					dn = getNode(x, y + 1).position - node.position
				if (x > 0)
					lt = getNode(x - 1, y).position - node.position
				node.normal = Vector3.ZERO
				if (up != null) {
					if (rt != null) node.normal = node.normal + rt.cross(up)
					if (lt != null) node.normal = node.normal + up.cross(lt)
				}
				if (dn != null) {
					if (rt != null) node.normal = node.normal + dn.cross(rt)
					if (lt != null) node.normal = node.normal + lt.cross(dn)
				}
				node.normal = node.normal.normalize()
			}
		}
		
	}
	
	
	fun getNode(x: Int, y: Int): Node {
		return data.get(x, y)
	}
	
	private fun addTrianglesOfRow(triangles: MutableList<Triangle>, y: Int, smoothNormals: Boolean) {
		for (x in 0..width - 1 - 1) {
			addTriangle(triangles, y, x, y + 1, x, y, x + 1, smoothNormals)
			addTriangle(triangles, y + 1, x, y + 1, x + 1, y, x + 1, smoothNormals)
		}
	}
	
	private fun addTriangle(triangles: MutableList<Triangle>, ya: Int, xa: Int,
	                        yb: Int, xb: Int,
	                        yc: Int, xc: Int, smoothNormals: Boolean) {
		val triangle = Triangle()
		triangle.a.position = getNode(xa, ya).position
		triangle.b.position = getNode(xb, yb).position
		triangle.c.position = getNode(xc, yc).position
		if (smoothNormals) {
			triangle.a.normal = getNode(xa, ya).normal
			triangle.b.normal = getNode(xb, yb).normal
			triangle.c.normal = getNode(xc, yc).normal
		} else {
			triangle.setNormalsByPlane()
		}
		triangles.add(triangle)
	}
	
	fun renderNormals() {
		gl().glBegin(GL.GL_LINES)
		Material.RED.use()
		for (y in 0..height - 1) {
			for (x in 0..width - 1) {
				val p = getNode(x, y).position
				val n = getNode(x, y).normal
				GlUtil.direct(p)
				GlUtil.direct(p + n.normalize())
			}
		}
		gl().glEnd()
	}
	
}
