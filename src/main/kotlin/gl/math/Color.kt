package gl.math

import gl.gfx.gl
import gl.gfx.texture.Pixel
import math.linearalgebra.MathUtil
import math.linearalgebra.VectorSpace

class Color constructor(val r: Double = 0.0,
                        val g: Double = 0.0,
                        val b: Double = 0.0,
                        val a: Double = 1.0) : VectorSpace<Color>() {
	
	fun glApply() {
		gl().glColor4d(r, g, b, a)
	}
	
	override fun plus(o: Color): Color {
		return Color(r + o.r, g + o.g, b + o.b, a + o.a)
	}
	
	override fun minus(o: Color): Color {
		return Color(r - o.r, g - o.g, b - o.b, a - o.a)
	}
	
	override fun times(o: Double): Color {
		return Color(r * o, g * o, b * o, a * o)
	}
	
	fun equal(o: Color): Boolean {
		return r == o.r && g == o.g && b == o.b && a == o.a
	}
	
	override fun almostEqual(o: Color): Boolean {
		return MathUtil.almostEqual(this.r, o.r) && MathUtil.almostEqual(this.g, o.g) &&
				MathUtil.almostEqual(this.b, o.b) && MathUtil.almostEqual(this.a, o.a)
	}
	
	override fun unaryMinus(): Color {
		return Color(-r, -g, -b, -a)
	}
	
	@JvmOverloads fun clamp(min: Double = 0.0, max: Double = 1.0): Color {
		val nr = MathUtil.clamp(r, min, max)
		val ng = MathUtil.clamp(g, min, max)
		val nb = MathUtil.clamp(b, min, max)
		val na = MathUtil.clamp(a, min, max)
		return Color(nr, ng, nb, na)
	}
	
	fun toPixel(): Pixel {
		return Pixel(toByte(r), toByte(g), toByte(b), toByte(a))
	}
	
	fun data(): DoubleArray {
		return doubleArrayOf(r, g, b, a)
	}
	
	fun floatData(): FloatArray {
		return floatArrayOf(r.toFloat(), g.toFloat(), b.toFloat(), a.toFloat())
	}
	
	override fun toString(): String {
		return "($r, $g, $b, $a)"
	}
	
	fun toRgbInt(): Int {
		return rgbIntFromInts(toInt(r), toInt(g), toInt(b))
	}
	
	private fun toInt(d: Double): Int {
		return MathUtil.clamp((d * 256.0).toInt(), 0, 255)
	}
	
	fun toAwtColor(): java.awt.Color {
		return java.awt.Color(toInt(r), toInt(g), toInt(b), toInt(a))
	}
	
	companion object {
		
		val ONE = Color(1.0, 1.0, 1.0)
		val ZERO = Color(0.0, 0.0, 0.0, 0.0)
		val BLACK = Color(0.0, 0.0, 0.0)
		val GRAY = Color(0.5, 0.5, 0.5)
		val WHITE = Color(1.0, 1.0, 1.0)
		val RED = Color(1.0, 0.0, 0.0)
		val GREEN = Color(0.0, 1.0, 0.0)
		val BLUE = Color(0.0, 0.0, 1.0)
		val YELLOW = Color(1.0, 1.0, 0.0)
		val PURPLE = Color(1.0, 0.0, 1.0)
		val CYAN = Color(0.0, 1.0, 1.0)
		val ORANGE = Color(1.0, 0.5, 1.0)
		val PINK = Color(0.0, 0.2667, 0.7333)
		val SKY = Color(0.5, 0.7, 0.9)
		val MELLOW = Color(0.9, 0.7, 0.5)
		val FOREST = Color(0.247, 0.498, 0.373)
		val SILVER = Color(0.784314, 0.784314, 0.784314)
		val GOLD = Color(0.862745, 0.745098, 0.0)
		
		fun rgbIntFromBytes(r: Byte, g: Byte, b: Byte): Int {
			return rgbIntFromInts(unsignByte(r), unsignByte(g), unsignByte(b))
		}
		
		fun rgbIntFromInts(r: Int, g: Int, b: Int): Int {
			var rgb = 255
			rgb = (rgb shl 8) + r
			rgb = (rgb shl 8) + g
			rgb = (rgb shl 8) + b
			return rgb
		}
		
		fun unsignByte(b: Byte): Int {
			return b.toInt() and 0xFF
		}
		
		fun toByte(d: Double): Byte {
			return MathUtil.clamp((256 * d).toInt(), 0, 255).toByte()
		}
		
		@JvmOverloads fun random(alpha: Double = 1.0): Color {
			val min = 0.0
			val max = 1.0
			return Color(
					MathUtil.randomDouble(min, max),
					MathUtil.randomDouble(min, max),
					MathUtil.randomDouble(min, max),
					alpha)
		}
	}
}
