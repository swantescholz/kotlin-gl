package math.linearalgebra

abstract class VectorSpacei<T : VectorSpacei<T>> {
	abstract operator fun unaryMinus(): T
	
	abstract operator fun plus(o: T): T
	
	abstract operator fun minus(o: T): T
	
	abstract operator fun times(o: Int): T
}

operator fun <T : VectorSpacei<T>> Int.times(you: VectorSpacei<T>): T = you * this