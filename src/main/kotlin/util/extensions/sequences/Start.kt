package util.extensions.sequences

import math.BIG1
import java.math.BigInteger
import java.util.stream.IntStream
import java.util.stream.Stream

fun IntStream.asSequence(): Sequence<Int> = this.iterator().asSequence()
fun IntStream.ass(): Sequence<Int> = this.iterator().asSequence()
fun <T> Stream<T>.asSequence(): Sequence<T> = this.iterator().asSequence()
fun <T> Stream<T>.ass(): Sequence<T> = this.asSequence()
fun <T> Array<T>.ass(): Sequence<T> = this.asSequence()
fun <T> Iterable<T>.ass(): Sequence<T> = this.asSequence()
fun <K, V> Map<K, V>.ass(): Sequence<Map.Entry<K, V>> = this.asSequence()

fun Int.seq(end: Int = Int.MAX_VALUE, step: Int = 1) = (this..end step step).asSequence()
fun Int.seq(end: Long, step: Long = 1L) = (this.toLong()..end step step).asSequence()
fun Int.seqDown(end: Int = Int.MIN_VALUE, step: Int = 1) = (this downTo end step step).asSequence()

fun Long.seq(end: Long = Long.MAX_VALUE, step: Long = 1L) = (this..end step step).asSequence()
fun Long.seq(end: Int, step: Int = 1) = (this..end step step.toLong()).asSequence()
fun Long.seqDown(end: Long = Long.MIN_VALUE, step: Long = 1L) = (this downTo end step step).asSequence()

fun BigInteger.seq(end: BigInteger? = null, step: BigInteger = BIG1): Sequence<BigInteger> {
	var iter = this - step
	return generateSequence {
		iter += step
		if (end != null && iter > end)
			return@generateSequence null
		return@generateSequence iter
	}
}