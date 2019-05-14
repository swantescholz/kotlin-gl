package gl.gfx.model

import gl.util.FileResourceManager
import util.FileUtil
import util.log.Log
import java.io.File
import java.util.*

class ModelManager private constructor(defaultLocation: File, defaultFilenameEnding: String) : FileResourceManager<Mesh>(defaultLocation, defaultFilenameEnding) {
	
	operator fun get(name: String): Model {
		for (ending in Arrays.asList("", ".obj", ".off")) {
			if (hasResource(name + ending))
				return Model(getResource(name + ending))
		}
		throw IllegalArgumentException("resource '$name' not found")
	}
	
	private fun loadOff(filename: String) {
		val mesh = Mesh.createFromOff(FileUtil.readFile(getFile(filename)))
		addMesh(filename, mesh)
	}
	
	private fun loadObj(filename: String) {
		val mesh = Mesh.createFromObj(FileUtil.readFile(getFile(filename)))
		addMesh(filename, mesh)
	}
	
	fun load(filename: String) {
		if (filename.endsWith(".off")) {
			loadOff(filename)
		} else if (filename.endsWith(".obj")) {
			loadObj(filename)
		} else {
			Log.errorAndThrow(IllegalArgumentException("bad filename, neither .off nor .obj: '$filename'"))
		}
	}
	
	private fun addMesh(filename: String, mesh: Mesh) {
		mesh.createVbo()
		addResource(filename, mesh)
	}
	
	companion object {
		
		val instance = ModelManager(File("res/model"), "")
	}
	
}
