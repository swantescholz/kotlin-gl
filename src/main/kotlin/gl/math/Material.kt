package gl.math

import gl.gfx.shader.uniform.UniformManager
import math.linearalgebra.MathUtil
import math.linearalgebra.VectorSpace

class Material : VectorSpace<Material> {
	
	val ambient: Color
	val diffuse: Color
	val specular: Color
	val shininess: Double
	
	constructor() {
		ambient = Color.WHITE
		diffuse = Color.WHITE
		specular = Color.WHITE
		shininess = 64.0
	}
	
	constructor(color: Color) : this(color, color, color) {
	}
	
	constructor(color: Color, shininess: Double) : this(color, color, color, shininess) {
	}
	
	@JvmOverloads constructor(ambient: Color, diffuse: Color, specular: Color, shininess: Double = 64.0) {
		this.ambient = ambient
		this.diffuse = diffuse
		this.specular = specular
		this.shininess = shininess
	}
	
	override fun unaryMinus(): Material {
		return Material(ambient.unaryMinus(), diffuse.unaryMinus(), specular.unaryMinus(), -shininess)
	}
	
	override fun plus(o: Material): Material {
		return Material(
				ambient.plus(o.ambient),
				diffuse.plus(o.diffuse),
				specular.plus(o.specular),
				shininess + o.shininess)
	}
	
	override fun minus(o: Material): Material {
		return Material(
				ambient.minus(o.ambient),
				diffuse.minus(o.diffuse),
				specular.minus(o.specular),
				shininess - o.shininess)
	}
	
	override fun times(o: Double): Material {
		return Material(
				ambient.times(o),
				diffuse.times(o),
				specular.times(o),
				shininess * o)
	}
	
	fun equal(o: Material): Boolean {
		return ambient.equal(o.ambient) && diffuse.equal(o.diffuse) &&
				specular.equal(o.specular) && shininess == o.shininess
	}
	
	override fun almostEqual(o: Material): Boolean {
		return ambient.almostEqual(o.ambient) && diffuse.almostEqual(o.diffuse) &&
				specular.almostEqual(o.specular) && MathUtil.almostEqual(shininess, o.shininess)
	}
	
	fun use() {
		UniformManager.material.value = this
	}
	
	override fun toString(): String {
		return "[$ambient, $diffuse, $specular, $shininess]"
	}
	
	companion object {
		
		val BLACK = Material(Color.BLACK)
		val GRAY = Material(Color.GRAY)
		val WHITE = Material(Color.WHITE)
		val RED = Material(Color.RED)
		val GREEN = Material(Color.GREEN)
		val BLUE = Material(Color.BLUE)
		val YELLOW = Material(Color.YELLOW)
		val PURPLE = Material(Color.PURPLE)
		val CYAN = Material(Color.CYAN)
		val ORANGE = Material(Color.ORANGE)
		val PINK = Material(Color.PINK)
		val SKY = Material(Color.SKY)
		val MELLOW = Material(Color.MELLOW)
		val FOREST = Material(Color.FOREST)
		val SILVER = Material(Color.SILVER, 96.0)
		val GOLD = Material(Color.GOLD, 96.0)
		val BRASS = Material(Color(0.33, 0.22, 0.03),
				Color(0.78, 0.57, 0.11), Color(0.99, .91, 0.81), 5.0)
	}
	
}
