package math.linearalgebra

import util.astTrue
import util.extensions.round
import util.extensions.sequences.seq

class Vectorn(val size: Int, fillFunction: (Int) -> Double = { 0.0 }) {
	
	constructor(c: Collection<Double>) : this(c.size) {
		val iter = c.iterator()
		for (i in 0..size - 1) {
			this[i] = iter.next()
		}
	}
	
	val data = Array(size, fillFunction)
	
	operator fun get(index: Int) = data[index]
	operator fun set(index: Int, value: Double) {
		data[index] = value
	}
	
	operator fun unaryMinus() = Vectorn(size, { -this[it] })
	operator fun plus(you: Vectorn) = Vectorn(size, { this[it] + you[it] })
	operator fun minus(you: Vectorn) = Vectorn(size, { this[it] - you[it] })
	operator fun times(you: Double) = Vectorn(size, { this[it] * you })
	operator fun mod(you: Double) = Vectorn(size, { this[it] % you })
	operator fun div(you: Double) = Vectorn(size, { this[it] / you })
	operator fun times(you: Matrixdn): Vectorn {
		astTrue(this.size == you.height)
		return Vectorn(you.width, { x ->
			0.seq(you.height - 1).map { y ->
				this[y] * you[x, y]
			}.sum()
		})
	}
	
	override fun toString(): String {
		var text = ""
		for (x in 0..size - 1) {
			text += this[x].toString() + " "
		}
		return text.removeSuffix(" ")
	}
	
	fun toPolynomialString(xChar: String = "x"): String {
		fun doubleToString(d: Double): String {
			if (d == d.round().toDouble())
				return "${d.round()}"
			return "$d"
		}
		return asSequence().withIndex().filter { it.value != 0.0 }
				.map {
					when (it.index) {
						0 -> doubleToString(it.value)
						1 -> "${doubleToString(it.value)}*$xChar"
						else -> "${doubleToString(it.value)}*$xChar^${it.index}"
					}
				}
				.map {
					if (it[0] != '-')
						return@map "+$it"
					return@map "$it"
				}
				.toCollection(arrayListOf<String>()).reversed().joinToString("").removePrefix("+")
	}
	
	private fun asSequence(): Sequence<Double> {
		return data.asSequence()
	}
	
	fun evaluatePolynomial(x: Double): Double {
		return this.asSequence().withIndex()
				.map { it.value * Math.pow(x, it.index.toDouble()) }.sum()
	}
	
	fun sum(): Double = data.asSequence().sum()
}

operator fun Double.times(you: Vectorn) = Vectorn(you.size, { you[it] * this })