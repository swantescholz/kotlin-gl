package gl.util

import util.FileUtil
import java.io.File

abstract class FileResourceManager<R> protected constructor(protected val defaultLocation: File,
                                                            protected val defaultFilenameEnding: String) : ResourceManager<R>() {
	
	fun getFile(simplePath: String): File {
		return File(defaultLocation, simplePath + defaultFilenameEnding)
	}
	
	fun readFile(simplePath: String): String {
		return FileUtil.readFile(getFile(simplePath))
	}
	
}
