package math.linearalgebra

import util.astEqual
import util.astGreaterEqual
import util.astTrue
import util.extensions.even
import util.extensions.sequences.seq

// indexed (x,y)
class MatrixL(val width: Int, val height: Int = width, initFunction: (Int, Int) -> Long = { x, y -> 0 }) {
	
	val data = Array(width, { x -> Array(height, { y -> initFunction(x, y) }) })
	
	constructor(you: MatrixL) : this(you.width, you.height, { x, y -> you[x, y] })
	
	operator fun get(x: Int, y: Int) = data[x][y]
	
	operator fun get(xyPair: Pair<Int, Int>) = data[xyPair.first][xyPair.second]
	
	operator fun set(x: Int, y: Int, value: Long) {
		data[x][y] = value
	}
	
	operator fun set(xyPair: Pair<Int, Int>, value: Long) {
		data[xyPair.first][xyPair.second] = value
	}
	
	
	operator fun unaryMinus() = MatrixL(width, height, { x, y -> -this[x, y] })
	operator fun plus(you: MatrixL) = MatrixL(width, height, { x, y -> this[x, y] + you[x, y] })
	operator fun minus(you: MatrixL) = MatrixL(width, height, { x, y -> this[x, y] - you[x, y] })
	operator fun times(you: Long) = MatrixL(width, height, { x, y -> this[x, y] * you })
	operator fun mod(you: Long) = MatrixL(width, height, { x, y -> this[x, y] % you })
	operator fun times(you: MatrixL): MatrixL {
		astTrue(this.width == you.height)
		return MatrixL(you.width, this.height, { x, y ->
			0.seq(width - 1).map {
				this[it, y] * you[x, it]
			}.sum()
		})
	}
	
	operator fun times(you: Vectorn): Vectorn {
		astTrue(this.width == you.size)
		return Vectorn(this.height, { y ->
			0.seq(width - 1).map { x ->
				this[x, y] * you[x]
			}.sum()
		})
	}
	
	fun modPow(exponent: Long, m: Long = Long.MAX_VALUE): MatrixL {
		astGreaterEqual(exponent, 0L)
		astEqual(width, height)
		var y = MatrixL.identity(width)
		if (exponent == 0L) {
			return y
		}
		var x = this
		var n = exponent
		while (n > 1) {
			if (n.even()) {
				x *= x
				n /= 2
			} else {
				y = x * y
				y %= m
				x *= x
				n = (n - 1) / 2
			}
			x %= m
		}
		return (x * y) % m
	}
	
	override fun toString(): String {
		var text = ""
		for (y in 0..height - 1) {
			var row = ""
			for (x in 0..width - 1) {
				row += this[x, y].toString() + " "
			}
			text += row.removeSuffix(" ") + "\n"
		}
		return text.removeSuffix("\n")
	}
	
	fun asSequence(): Sequence<Triple<Int, Int, Long>> {
		return 0.seq(height - 1).map { y ->
			0.seq(width - 1).map { x ->
				Triple(x, y, this[x, y])
			}
		}.flatten()
	}
	
	companion object {
		fun identity(width: Int): MatrixL {
			return MatrixL(width, width, { x, y ->
				if (x == y)
					return@MatrixL 1L
				0L
			})
		}
	}
}

operator fun Long.times(you: MatrixL) = MatrixL(you.width, you.height, { x, y -> you[x, y] * this })