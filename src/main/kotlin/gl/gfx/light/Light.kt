package gl.gfx.light

import gl.gfx.shader.uniform.UniformManager
import gl.math.Color
import gl.math.Material
import gl.util.IndexPool

abstract class Light protected constructor() : ILight {
	
	override var ambient = Color.WHITE
	override var diffuse = Color.WHITE
	override var specular = Color.WHITE
	override var id = -1
		protected set
	override var isEnabled = false
		set(enabled) {
			val pool = INDEX_POOL
			if (this.isEnabled == enabled) {
				return
			} else if (enabled) {
				id = INDEX_POOL.acquire()
			} else {
				INDEX_POOL.release(id)
			}
			field = enabled
		}
	
	val material: Material
		get() = Material(ambient, diffuse, specular)
	
	init {
		isEnabled = true
	}
	
	fun shine() {
		UniformManager.applyLight(this)
	}
	
	companion object {
		
		val MAX_NUMBER = 4
		
		private val INDEX_POOL = IndexPool()
		
		init {
			for (i in 0..MAX_NUMBER - 1) {
				INDEX_POOL.add(i)
			}
		}
	}
}
