package math

import util.extensions.signum

fun productOverflows(a: Long, b: Long): Boolean {
	val maximum = if (a.signum() == b.signum()) Long.MAX_VALUE else Long.MIN_VALUE
	if (a == 0L || b == 0L)
		return false
	if (b > 0)
		return b > maximum / a
	return b < maximum / a
}

