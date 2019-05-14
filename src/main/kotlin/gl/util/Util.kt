package gl.util

object Util {
	
	fun wait(seconds: Double) {
		try {
			Thread.sleep((1000 * seconds).toLong())
		} catch (e: InterruptedException) {
			e.printStackTrace()
		}
		
	}
	
	
}
