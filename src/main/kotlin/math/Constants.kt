package math

import util.extensions.pow
import util.extensions.sequences.seq
import java.math.BigDecimal
import java.math.BigInteger

val DMAX = Double.MAX_VALUE
val DMIN = -Double.MAX_VALUE
val FMAX = Float.MAX_VALUE
val FMIN = -Float.MAX_VALUE
val LMAX = Long.MAX_VALUE
val LMIN = Long.MIN_VALUE
val IMAX = Int.MAX_VALUE
val IMIN = Int.MIN_VALUE
val SMAX = Short.MAX_VALUE
val SMIN = Short.MIN_VALUE
val BMAX = Byte.MAX_VALUE
val BMIN = Byte.MIN_VALUE
val MILLI = 1 / 1000.0
val MICRO = 1 / 1000000.0
val NANO = 1 / 1000000000.0

val SQRT2 = Math.sqrt(2.0)
val SQRT3 = Math.sqrt(3.0)
val SQRT5 = Math.sqrt(5.0)
val PHI = (1 + SQRT5) / 2.0
val PI = Math.PI
val E = Math.E
val DEGREE_TO_RADIAN_FACTOR = Math.PI / 180.0
val RADIAN_TO_DEGREE_FACTOR = 180.0 / Math.PI

val LOG2 = Math.log(2.0)
val LOG10 = Math.log(10.0)

val BIG = 0L.seq(100).map { BigInteger.valueOf(it) }.toCollection(arrayListOf<BigInteger>())
val BIG0 = BigInteger.valueOf(0)
val BIG1 = BigInteger.valueOf(1)
val BIGN1 = BigInteger.valueOf(-1)
val BIG2 = BigInteger.valueOf(2)
val BIG3 = BigInteger.valueOf(3)
val BIG4 = BigInteger.valueOf(4)
val BIG5 = BigInteger.valueOf(5)
val BIG10 = BigInteger.valueOf(10)

val BIGD = 0L.seq(100).map { BigDecimal.valueOf(it) }.toCollection(arrayListOf<BigDecimal>())
val BIGD0 = BigDecimal.valueOf(0)
val BIGD1 = BigDecimal.valueOf(1)
val BIGDN1 = BigDecimal.valueOf(-1)
val BIGD2 = BigDecimal.valueOf(2)
val BIGD10 = BigDecimal.valueOf(10)

val BYTE0 = 0.toByte()
val BYTE1 = 1.toByte()
val BYTE2 = 2.toByte()
val SHORT0 = 0.toShort()
val SHORT1 = 1.toShort()
val SHORT2 = 2.toShort()
val DEFAULT_EPSILON = 0.00001

val POWS10 = (0L..18).map { 10L.pow(it) }.toCollection(arrayListOf<Long>())
val POWS2 = (0L..63).map { 2L.pow(it) }.toCollection(arrayListOf<Long>())
val POW2_63_SQRT = Math.sqrt(2L.pow(63).toDouble()).toLong()
val BIG10_POWS = (0L..118).map { BIG10.pow(it.toInt()) }.toCollection(arrayListOf<BigInteger?>())
val BIG2_POWS = (0L..663).map { BIG2.pow(it.toInt()) }.toCollection(arrayListOf<BigInteger?>())
val BIGD10_POWS = (0L..118).map { BIGD10.pow(it.toInt()) }.toCollection(arrayListOf<BigDecimal?>())
val BIGD2_POWS = (0L..663).map { BIGD2.pow(it.toInt()) }.toCollection(arrayListOf<BigDecimal?>())


val DIGITS_10hs = hashSetOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
val DIGITS_9hs = hashSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
val DIGITS_10al = arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
val DIGITS_9al = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

val THOUSAND = 1000L
val MILLION = 1000000L
val BILLION = 1000000000L
val TRILLION = 1000000000000L

