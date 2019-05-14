package game

import math.linearalgebra.Coordi

enum class Direction(val offset: Coordi) {
	UP(Coordi(0, 1)),
	RIGHT(Coordi(1, 0)),
	DOWN(Coordi(0, -1)),
	LEFT(Coordi(-1, 0)),
}