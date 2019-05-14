package gl.gfx.texture

import gl.math.Color
import math.linearalgebra.MathUtil

class Pixel @JvmOverloads constructor(var r: Byte, var g: Byte, var b: Byte, a: Byte = 255.toByte()) {
	var a = 255.toByte()
	
	init {
		this.a = a
	}
	
	fun copy(): Pixel {
		return Pixel(r, g, b, a)
	}
	
	override fun toString(): String {
		return "(" + MathUtil.unsignByte(r) + "," + MathUtil.unsignByte(g) + "," + MathUtil.unsignByte(b) + "," + MathUtil.unsignByte(a) + ")"
	}
	
	fun toColor(): Color {
		return Color(MathUtil.byteToDouble(r), MathUtil.byteToDouble(g), MathUtil.byteToDouble(b), MathUtil.byteToDouble(a))
	}
	
	fun set(pixel: Pixel) {
		set(pixel.r, pixel.g, pixel.b, pixel.a)
	}
	
	operator fun set(r: Byte, g: Byte, b: Byte, a: Byte) {
		this.r = r
		this.g = g
		this.b = b
		this.a = a
	}
	
}
