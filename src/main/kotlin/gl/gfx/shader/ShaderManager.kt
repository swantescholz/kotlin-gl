package gl.gfx.shader

import gl.util.FileResourceManager
import util.FileUtil
import java.io.File
import javax.media.opengl.GL

class ShaderManager private constructor(defaultLocation: File, defaultFilenameEnding: String) : FileResourceManager<Shader>(defaultLocation, defaultFilenameEnding) {
	
	private var headerBoth: String? = null
	private var headerVert: String? = null
	private var headerFrag: String? = null
	
	init {
		reloadHeaders()
	}
	
	fun reloadHeaders() {
		headerBoth = FileUtil.readFile(getFile("header-both.frag"))
		headerVert = FileUtil.readFile(getFile("header.vert"))
		headerFrag = FileUtil.readFile(getFile("header.frag"))
	}
	
	fun getHeaderLineCountForShaderType(shaderType: Int): Int {
		if (shaderType == GL.GL_FRAGMENT_SHADER) {
			return headerBoth!!.count { it == '\n' } + headerFrag!!.count { it == '\n' }
		}
		return headerBoth!!.count { it == '\n' } + headerVert!!.count { it == '\n' }
	}
	
	fun loadVert(simplePath: String): VertexShader {
		return loadVert(simplePath, simplePath)
	}
	
	fun loadFrag(simplePath: String): FragmentShader {
		return loadFrag(simplePath, simplePath)
	}
	
	fun loadVert(futureId: String, simplePath: String): VertexShader {
		val vertexShader = VertexShader(simplePath + ".vert")
		val source = headerBoth + headerVert + readFile(simplePath + ".vert")
		loadShaderSource(futureId, vertexShader, source)
		return vertexShader
	}
	
	fun loadFrag(futureId: String, simplePath: String): FragmentShader {
		val fragmentShader = FragmentShader(simplePath + ".frag")
		val source = headerBoth + headerFrag + readFile(simplePath + ".frag")
		loadShaderSource(futureId, fragmentShader, source)
		return fragmentShader
	}
	
	private fun loadShaderSource(futureId: String, shader: Shader, source: String) {
		shader.setSource(source)
		dispose(futureId)
		addResource(futureId, shader)
	}
	
	fun dispose(shaderId: String) {
		if (hasResource(shaderId)) {
			getResource(shaderId).dispose()
			removeResource(shaderId)
		}
	}
	
	fun getShader(shaderId: String): Shader {
		return getResource(shaderId)
	}
	
	companion object {
		val instance = ShaderManager(File("res/shader"), "")
		
		init {
			instance.loadFrag("f.default", "default")
			instance.loadVert("v.default", "default")
		}
		
	}
}
