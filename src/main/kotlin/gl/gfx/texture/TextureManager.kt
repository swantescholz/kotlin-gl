package gl.gfx.texture

import gl.util.FileResourceManager
import util.log.Log

import java.io.File
import java.io.IOException

class TextureManager private constructor(defaultLocation: File, defaultFilenameEnding: String) : FileResourceManager<TextureData>(defaultLocation, defaultFilenameEnding) {
	
	operator fun get(name: String): Texture {
		if (!hasResource(name)) {
			Log.debug("texture '$name' not loaded before getting. loading it now.")
			load(name)
		}
		return Texture(getResource(name))
	}
	
	fun load(filename: String, transparentBorderSize: Int = 0): Texture {
		val file = getFile(filename)
		try {
			val grid = PixelGrid.createFromFile(file, transparentBorderSize)
			addResource(filename, TextureData(grid))
		} catch (e: IOException) {
			Log.errorAndThrow("file $file does not exist.")
		}
		return get(filename)
	}
	
	companion object {
		val instance = TextureManager(File("res/texture"), ".png")
	}
	
}
