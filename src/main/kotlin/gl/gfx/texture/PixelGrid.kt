package gl.gfx.texture

import gl.math.Dimension
import gl.util.DimensionedObject
import gl.util.StringUtil
import util.datastructures.Array2
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.Buffer
import java.nio.ByteBuffer
import javax.imageio.ImageIO

class PixelGrid constructor(w: Int, h: Int) : Array2<Pixel>(w, h,
		{ x, y -> Pixel(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE) }), DimensionedObject {
	
	
	constructor(dim: Dimension) : this(dim.width, dim.height) {
	}
	
	
	fun toBuffer(): Buffer {
		val buffer = ByteBuffer.allocate(4 * width * height)
		for (y in 0..height - 1) {
			for (x in 0..width - 1) {
				val pixel = get(x, y)
				buffer.put(pixel.r)
				buffer.put(pixel.g)
				buffer.put(pixel.b)
				buffer.put(pixel.a)
			}
		}
		buffer.rewind()
		return buffer
	}
	
	override fun toString(): String {
		var str = ""
		str += dimension.toString() + StringUtil.NEWLINE
		for (y in 0..height - 1) {
			for (x in 0..width - 1) {
				val pixel = get(x, y)
				str += Dimension(x, y).toString() + " " + pixel.toString()
				str += StringUtil.NEWLINE
			}
		}
		return str
	}
	
	override val dimension: Dimension
		get() = Dimension(width, height)
	
	companion object {
		
		
		@Throws(IOException::class)
		fun createFromFile(file: File, transparentBorderSize: Int = 0): PixelGrid {
			val image = ImageIO.read(file)
			return createFromBufferedImage(image, transparentBorderSize)
		}
		
		fun createFromBufferedImage(image: BufferedImage, transparentBorderSize: Int = 0): PixelGrid {
			val raster = image.data
			val colors = IntArray(4)
			if (transparentBorderSize > 0) {
				val grid = PixelGrid(raster.width + 2 * transparentBorderSize, raster.height + 2 * transparentBorderSize)
				for (y in 0..raster.height - 1) {
					for (x in 0..raster.width - 1) {
						colors[3] = 255 //opaque is default, when no alpha given
						raster.getPixel(x, y, colors)
						val pixel = Pixel(colors[0].toByte(), colors[1].toByte(), colors[2].toByte(), colors[3].toByte())
						grid[x + transparentBorderSize, raster.height - y - 1 + transparentBorderSize] = pixel
					}
				}
				fun makeTransparent(pixel: Pixel) = Pixel(pixel.r, pixel.g, pixel.b, 0)
				for (y in transparentBorderSize..grid.height - 1 - transparentBorderSize) {
					for (x in 0..transparentBorderSize - 1) {
						grid[x, y] = makeTransparent(grid[transparentBorderSize, y])
						grid[grid.width - 1 - x, y] = makeTransparent(grid[grid.width - 1 - transparentBorderSize, y])
					}
				}
				for (x in 0..grid.width - 1) {
					for (y in 0..transparentBorderSize - 1) {
						grid[x, y] = makeTransparent(grid[x, transparentBorderSize])
						grid[x, grid.height - 1 - y] = makeTransparent(grid[x, grid.height - 1 - transparentBorderSize])
					}
				}
				return grid
			} else {
				val grid = PixelGrid(raster.width, raster.height)
				for (y in 0..grid.height - 1) {
					for (x in 0..grid.width - 1) {
						colors[3] = 255 //opaque is default, when no alpha given
						raster.getPixel(x, y, colors)
						val pixel = Pixel(colors[0].toByte(), colors[1].toByte(), colors[2].toByte(), colors[3].toByte())
						grid[x, grid.height - y - 1] = pixel
					}
				}
				return grid
			}
		}
	}
	
}
