package gl.gfx.shader

import gl.util.ResourceManager

class ProgramManager private constructor() : ResourceManager<Program>() {
	
	fun linkProgram(newProgramId: String, vertShaderId: String, fragShaderId: String): Program {
		dispose(newProgramId)
		val vertShader = shaders.getShader(vertShaderId)
		val fragShader = shaders.getShader(fragShaderId)
		val program = Program()
		program.attachShader(vertShader)
		program.attachShader(fragShader)
		program.link()
		addResource(newProgramId, program)
		return program
	}
	
	fun dispose(programId: String) {
		if (hasResource(programId)) {
			getResource(programId).dispose()
			removeResource(programId)
		}
	}
	
	operator fun get(programId: String): Program {
		return getResource(programId)
	}
	
	companion object {
		val instance = ProgramManager()
		val DEFAULT: Program
		private val shaders = ShaderManager.instance
		
		init {
			DEFAULT = instance.linkProgram("default", "v.default", "f.default")
		}
	}
}
