package gl.gfx

import java.util.*
import javax.media.opengl.GL


open class GlOperator {
	
	protected var gl: GL
	
	init {
		gl = gl()
		operators.add(this)
	}
	
	companion object {
		private val operators = ArrayList<GlOperator>()
		
		internal fun updateAll() {
			val gl = gl()
			for (operator in operators) {
				operator.gl = gl
			}
		}
	}
	
	
}
