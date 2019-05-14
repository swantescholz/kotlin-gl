package gl.gfx.model

import gl.math.Triangle
import math.linearalgebra.Vector3
import java.util.*

// flat grid, from (0,0,0) to (1,1,0), no texcoords set
class SimpleGrid private constructor() {
	private val vbo = Vbo()
	
	constructor(numTilesW: Int, numTilesH: Int) : this() {
		val hdiff = 1.0 / numTilesH
		val wdiff = 1.0 / numTilesW
		val triangles = ArrayList<Triangle>()
		for (h in 0..numTilesH - 1) {
			val y = h * hdiff
			for (w in 0..numTilesW - 1) {
				val x = w * wdiff
				val a = Vector3(x, y)
				val b = Vector3(x + wdiff, y)
				val c = Vector3(x + wdiff, y + hdiff)
				val d = Vector3(x, y + hdiff)
				triangles.add(Triangle(a, b, d))
				triangles.add(Triangle(b, c, d))
			}
		}
		vbo.create(triangles)
	}
	
	fun render() {
		vbo.render()
	}
}