package gl.gfx.model

import math.linearalgebra.Vector3
import util.datastructures.Array2
import util.log.Log

class Nurbs(private val wSubDivCount: Int, private val hSubDivCount: Int) : RenderableObject() {
	private var controlPoints: Array2<ControlPoint>? = null
	private var grid: Grid? = null
	private val vbo = Vbo()
	
	private inner class ControlPoint {
		internal var position = Vector3()
		internal var tangentW = Vector3()
		internal var tangentH = Vector3()
	}
	
	fun create(controlPointGrid: Grid) {
		dispose()
		val nodew = controlPointGrid.width
		val nodeh = controlPointGrid.height
		controlPoints = Array2(nodew, nodeh, { x, y ->
			val point = ControlPoint()
			point.position = controlPointGrid.getNode(x, y).position
			point
		})
		computeTangentsCatmull(0.5)
		createGrid()
		fillVbo()
	}
	
	override fun draw() {
		vbo.render()
//		grid!!.renderControlNodes(ModelManager.instance["cube"])
//		grid!!.renderNormals()
	}
	
	fun dispose() {
		vbo.dispose()
	}
	
	private fun fillVbo() {
		val triangles = grid!!.createTriangles(true)
		Log.debug("NURBS creation, #triangles: " + triangles.size)
		vbo.create(triangles)
	}
	
	private fun createGrid() {
		val gridWidth = computeGridWidth()
		val gridHeight = computeGridHeight()
		grid = Grid(gridWidth, gridHeight)
		fillGrid()
	}
	
	private fun computeGridHeight(): Int {
		return (controlPoints!!.height - 3) * (hSubDivCount + 1) + 1
	}
	
	private fun computeGridWidth(): Int {
		return (controlPoints!!.width - 3) * (wSubDivCount + 1) + 1
	}
	
	private fun fillGrid() {
		for (y in 1..controlPoints!!.height - 2 - 1) {
			for (x in 1..controlPoints!!.width - 2 - 1) {
				fillGridQuad(x, y)
			}
		}
	}
	
	private fun fillGridQuad(x: Int, y: Int) {
		val uplt = controlPoints!!.get(x, y)
		val uprt = controlPoints!!.get(x + 1, y)
		val dnlt = controlPoints!!.get(x, y + 1)
		val dnrt = controlPoints!!.get(x + 1, y + 1)
		val gridxa = (x - 1) * (wSubDivCount + 1)
		var gridxb = x * (wSubDivCount + 1)
		val gridya = (y - 1) * (hSubDivCount + 1)
		var gridyb = y * (hSubDivCount + 1)
		if (x + 3 == controlPoints!!.width) {
			gridxb += 1
		}
		if (y + 3 == controlPoints!!.height) {
			gridyb += 1
		}
		for (gridx in gridxa..gridxb - 1) {
			for (gridy in gridya..gridyb - 1) {
				val fx = (gridx - gridxa).toDouble() / (wSubDivCount + 1)
				val fy = (gridy - gridya).toDouble() / (hSubDivCount + 1)
				val gridPoint = interpolate(uplt, uprt, dnlt, dnrt, fx, fy)
				grid!!.getNode(gridx, gridy).position = gridPoint
			}
		}
	}
	
	private fun interpolate(uplt: ControlPoint, uprt: ControlPoint, dnlt: ControlPoint, dnrt: ControlPoint, fx: Double, fy: Double): Vector3 {
		val posupm = uplt.position.interpolateHermite(uplt.tangentW,
				uprt.position, uprt.tangentW, fx)
		val posdnm = dnlt.position.interpolateHermite(dnlt.tangentW,
				dnrt.position, dnrt.tangentW, fx)
		val tangentupm = uplt.tangentH.interpolateLinear(uprt.tangentH, fx)
		val tangentdnm = dnlt.tangentH.interpolateLinear(dnrt.tangentH, fx)
		return posupm.interpolateHermite(tangentupm, posdnm, tangentdnm, fy)
	}
	
	private fun computeTangentsCatmull(factor: Double) {
		for (y in 1..controlPoints!!.height - 1 - 1) {
			for (x in 1..controlPoints!!.width - 1 - 1) {
				val point = controlPoints!!.get(x, y)
				val north = controlPoints!!.get(x, y - 1)
				val east = controlPoints!!.get(x + 1, y)
				val south = controlPoints!!.get(x, y + 1)
				val west = controlPoints!!.get(x - 1, y)
				point.tangentW = east.position.minus(west.position).times(factor)
				point.tangentH = south.position.minus(north.position).times(factor)
			}
		}
	}
	
	
}
