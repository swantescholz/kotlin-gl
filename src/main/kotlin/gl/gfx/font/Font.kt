package gl.gfx.font

import gl.gfx.texture.PixelGrid
import gl.gfx.texture.Texture
import gl.gfx.texture.TextureData
import gl.math.Color
import util.extensions.float
import java.awt.font.FontRenderContext
import java.awt.image.BufferedImage
import java.io.File

class Font(val fontSize: Int, fontName: String, fontType: Int = java.awt.Font.PLAIN) {
	companion object {
		val FONT_FILE_PATH = "res/font/"
		//		val DEFAULT = Font(12, "FreeSans")
		val DEFAULT = Font(24, "DejaVuSansMono")
	}
	
	private val awtFont: java.awt.Font
	
	init {
//		awtFont = java.awt.Font("Serif", java.awt.Font.BOLD, size)
		val baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, File("$FONT_FILE_PATH$fontName.ttf"))
		awtFont = baseFont.deriveFont(fontType, fontSize.float)
	}
	
	fun createTexturedRectangle(text: String, color: Color): TexturedRectangle {
		val frc = FontRenderContext(null, true, true)
		
		val bounds = awtFont.getStringBounds(text, frc)
		val w = bounds.width.toInt() + 1
		val h = bounds.height.toInt()
		
		val image = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
		val g = image.createGraphics()
		g.color = Color.ZERO.toAwtColor()
		g.fillRect(0, 0, w, h)
		g.color = color.toAwtColor()
		g.font = awtFont
		g.drawString(text, bounds.x.toFloat(), (-bounds.y).toFloat())
		g.dispose()
		
		val grid = PixelGrid.createFromBufferedImage(image)
		val data = TextureData(grid)
		return TexturedRectangle(Texture(data), w / h.toDouble())
	}
	
}
