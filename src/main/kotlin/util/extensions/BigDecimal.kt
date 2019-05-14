package util.extensions

import math.BIGD2
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun BigDecimal.sqrt(scale: Int = 20): BigDecimal {
	var x0 = BigDecimal.valueOf(0);
	var x1 = BigDecimal(Math.sqrt(this.toDouble()));
	val rmode = RoundingMode.HALF_UP
	while (!x0.equals(x1)) {
		x0 = x1;
		x1 = this.divide(x0, scale, rmode);
		x1 = x1.add(x0);
		x1 = x1.divide(BIGD2, scale, rmode);
		
	}
	return x1;
}

fun BigDecimal.ceil(): BigInteger {
	return this.setScale(0, RoundingMode.CEILING).toBigInteger()
}

fun BigDecimal.floor(): BigInteger {
	return this.setScale(0, RoundingMode.FLOOR).toBigInteger()
}

fun BigDecimal.bigIntRound(): BigInteger {
	return this.setScale(0, RoundingMode.HALF_UP).toBigInteger()
}
