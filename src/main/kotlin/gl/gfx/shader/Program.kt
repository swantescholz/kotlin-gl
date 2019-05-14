package gl.gfx.shader

import gl.gfx.gl
import gl.gfx.shader.uniform.UniformManager
import java.util.*
import javax.media.opengl.GL

class Program {
	
	private val gl: GL
	private val programId: Int
	
	init {
		gl = gl()
		programId = gl.glCreateProgram()
	}
	
	fun attachShader(shader: Shader) {
		gl.glAttachShader(programId, shader.id)
	}
	
	fun link() {
		gl.glLinkProgram(programId)
	}
	
	fun use() {
		currentlyUsedProgram = this
		gl.glUseProgram(programId)
		UniformManager.applyAllDefaultUniformsToCurrentShader()
	}
	
	fun dispose() {
		gl.glDeleteProgram(programId)
	}
	
	companion object {
		private val stack = Stack<Program>()
		private var currentlyUsedProgram: Program? = null
		
		fun useNone() {
			gl().glUseProgram(0)
			currentlyUsedProgram = null
		}
		
		fun push() {
			if (currentlyUsedProgram != null) {
				stack.push(currentlyUsedProgram)
			}
		}
		
		fun pop() {
			if (!stack.empty()) {
				stack.pop().use()
			}
		}
	}
}
