package gl.gfx.shader

import gl.gfx.gl
import util.log.Log
import javax.media.opengl.GL

open class Shader protected constructor(val nameOrPath: String, val shaderType: Int) {
	
	val id: Int
	
	init {
		this.id = gl().glCreateShader(shaderType)
	}
	
	@Throws(CompilationFailedException::class)
	fun setSource(source: String) {
		gl().glShaderSource(id, 1, arrayOf(source), intArrayOf(source.length), 0)
		gl().glCompileShader(id)
		checkErrors()
	}
	
	@Throws(CompilationFailedException::class)
	private fun checkErrors() {
		val buffer = IntArray(1)
		gl().glGetShaderiv(id, GL.GL_COMPILE_STATUS, buffer, 0)
		if (buffer[0] == 0) {
			gl().glGetShaderiv(id, GL.GL_INFO_LOG_LENGTH, buffer, 0)
			
			val maxLength = buffer[0]
			val errorLog = ByteArray(maxLength)
			gl().glGetShaderInfoLog(id, maxLength, buffer, 0, errorLog, 0)
			
			dispose()
			val headerLineCount = ShaderManager.instance.getHeaderLineCountForShaderType(shaderType)
			val errorString = String(errorLog).take(errorLog.size - 1)
			val updatedErrorMessage = errorString.lines().filter { it.length > 0 }.map { line ->
				val lineNumber = Regex("""\d*\((\d*)\)""").find(line)?.destructured?.component1()?.toInt()
						?: return@map line
				return@map Regex("""(\d*)\((\d*)\)""").replaceFirst(line,
						"\$1:${lineNumber - headerLineCount}")
			}.joinToString(separator = "\n")
			Log.errorAndThrow(CompilationFailedException("Shader '$nameOrPath' did not load: " + updatedErrorMessage))
		}
	}
	
	fun dispose() {
		gl().glDeleteShader(id)
	}
}
