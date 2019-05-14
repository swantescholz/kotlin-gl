package util.extensions

import util.extensions.sequences.seq
import util.extensions.sequences.takeUntilNull

infix fun IntRange.step(stepSize: Double): Sequence<Double> {
	return this.start.toDouble()..this.endInclusive.toDouble() step stepSize
}

infix fun LongRange.step(stepSize: Double): Sequence<Double> {
	return this.start.toDouble()..this.endInclusive.toDouble() step stepSize
}

infix fun ClosedRange<Double>.step(stepSize: Double): Sequence<Double> {
	var value = this.start - stepSize
	return 1.seq().map {
		value += stepSize
		if (value > this.endInclusive)
			return@map null
		value
	}.takeUntilNull()
}

