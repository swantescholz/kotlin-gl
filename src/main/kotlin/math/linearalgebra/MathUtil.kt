package math.linearalgebra

import gl.gfx.GlUtil
import math.linearalgebra.collisions.CollisionUtil
import java.util.*

object MathUtil {
	
	val EPSILON = 0.000001
	
	fun createVector2List(vararg coords: Double): List<Vector2> {
		val points = ArrayList<Vector2>(coords.size / 2)
		var i = 0
		while (i < coords.size) {
			points.add(Vector2(coords[i], coords[i + 1]))
			i += 2
		}
		return points
	}
	
	fun toDegree(radian: Double): Double {
		return radian / (Math.PI * 2) * 360.0
	}
	
	fun toRadian(degree: Double): Double {
		return degree * (Math.PI * 2) / 360.0
	}
	
	fun interpolate(a: Double, b: Double, t: Double): Double {
		return (1 - t) * a + t * b
	}
	
	fun clamp(s: Int, min: Int, max: Int): Int {
		if (s < min) return min
		if (s > max) return max
		return s
	}
	
	fun clamp(s: Double, min: Double, max: Double): Double {
		if (s < min) return min
		if (s > max) return max
		return s
	}
	
	fun randomInt(min: Int, max: Int): Int {
		return (Math.random() * (max - min + 1)).toInt() + min
	}
	
	fun randomDouble(min: Double, max: Double): Double {
		return Math.random() * (max - min) + min
	}
	
	fun almostEqual(a: Double, b: Double): Boolean {
		return Math.abs(b - a) < EPSILON
		
	}
	
	fun clamp(d: Double): Double {
		return clamp(d, 0.0, 1.0)
	}
	
	fun unsignByte(b: Byte): Int {
		return b.toInt() and 0xFF
	}
	
	fun byteToDouble(b: Byte): Double {
		val i = unsignByte(b)
		return i / 255.0
	}
	
	//lower left = (-.5,-.5*height/width), upper right = (.5,.5*height/width), center = (0,0)
	fun getScreenCoordinates(worldCoordinate: Vector3, viewProjection: Matrix, ratio: Double): Vector2? {
		val p = viewProjection.transform(Vector4(worldCoordinate, 1.0))
		val h = p.z * 2.0 * Math.tan(GlUtil.DEFAULT_FOV / 2)
		if (Math.abs(p.x) < p.w && Math.abs(p.y) < p.w && 0 < p.z && p.z < p.w) {
			return Vector2(p.x, p.y / ratio).div(2 * p.w)
		}
		return null
	}
	
	
	fun isPolygonClockwise(vertices: List<Vector2>): Boolean {
		val iterator = vertices.iterator()
		var sum = 0.0
		var a = iterator.next()
		while (iterator.hasNext()) {
			val b = iterator.next()
			sum += (b.x - a.x) * (a.y + b.y)
			a = b
		}
		val b = vertices[0]
		sum += (b.x - a.x) * (a.y + b.y)
		return sum >= 0
	}
	
	fun isPolygonDegenerate(vertices: List<Vector2>): Boolean {
		val ita = vertices.iterator()
		var a = ita.next()
		while (ita.hasNext()) {
			val b = ita.next()
			val itb = vertices.iterator()
			var c: Vector2 = getFirstVertexAfter(b, itb)!! // todo: !!safe?
			while (itb.hasNext()) {
				val d = itb.next()
				if (CollisionUtil.intersectLines(a, b, c, d) != null) {
					return true
				}
				c = d
			}
			a = b
		}
		return false
	}
	
	private fun getFirstVertexAfter(lastVertex: Vector2, iterator: Iterator<Vector2>): Vector2? {
		while (lastVertex !== iterator.next()) {
		}
		if (!iterator.hasNext()) {
			return null
		}
		return iterator.next()
	}
}


