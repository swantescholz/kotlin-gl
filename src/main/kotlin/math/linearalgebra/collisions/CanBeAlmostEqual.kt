package math.linearalgebra.collisions

interface CanBeAlmostEqual<T> {
	
	fun almostEqual(other: T): Boolean
	
}
