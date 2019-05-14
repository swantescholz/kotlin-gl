package gl.math

class Dimension(var width: Int, val height: Int) {
	
	val size: Int
		get() = width * height
	val aspectRatio: Double
		get() = width.toDouble() / height
	
	override fun toString(): String {
		return "($width, $height)"
	}
}
