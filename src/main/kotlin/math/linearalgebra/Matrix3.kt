package math.linearalgebra

import gl.util.StringUtil

class Matrix3 : VectorSpace<Matrix3> {
	private val m = DoubleArray(ELEMENT_COUNT)
	
	constructor() {
		
	}
	
	constructor(m: DoubleArray) {
		System.arraycopy(m, 0, this.m, 0, ELEMENT_COUNT)
	}
	
	constructor(
			m00: Double, m01: Double, m02: Double,
			m10: Double, m11: Double, m12: Double,
			m20: Double, m21: Double, m22: Double) {
		m[0] = m00
		m[1] = m01
		m[2] = m02
		m[3] = m10
		m[4] = m11
		m[5] = m12
		m[6] = m20
		m[7] = m21
		m[8] = m22
	}
	
	fun copy(): Matrix3 {
		val n = Matrix3()
		System.arraycopy(m, 0, n.m, 0, ELEMENT_COUNT)
		return n
	}
	
	override fun unaryMinus(): Matrix3 {
		val n = copy()
		for (i in 0..ELEMENT_COUNT - 1) {
			n.m[i] = -m[i]
		}
		return n
	}
	
	
	private fun getIndex(row: Int, col: Int): Int {
		return row * COLUMN_COUNT + col
	}
	
	operator fun get(row: Int, col: Int): Double {
		return m[getIndex(row, col)]
	}
	
	// *********************************
	
	override fun plus(o: Matrix3): Matrix3 {
		val n = copy()
		for (i in 0..ELEMENT_COUNT - 1) {
			n.m[i] += o.m[i]
		}
		return n
	}
	
	override operator fun minus(o: Matrix3): Matrix3 {
		val n = copy()
		for (i in 0..ELEMENT_COUNT - 1) {
			n.m[i] -= o.m[i]
		}
		return n
	}
	
	override operator fun times(o: Double): Matrix3 {
		val n = copy()
		for (i in 0..ELEMENT_COUNT - 1) {
			n.m[i] *= o
		}
		return n
	}
	
	fun equal(n: Matrix3): Boolean {
		return m[0] == n.m[0] && m[1] == n.m[1] && m[2] == n.m[2] && m[3] == n.m[3] &&
				m[4] == n.m[4] && m[5] == n.m[5] && m[6] == n.m[6] && m[7] == n.m[7] &&
				m[8] == n.m[8]
	}
	
	override fun almostEqual(o: Matrix3): Boolean {
		for (i in 0..ELEMENT_COUNT - 1) {
			if (!MathUtil.almostEqual(m[i], o.m[i]))
				return false
		}
		return true
	}
	
	operator fun times(b: Matrix3): Matrix3 {
		val a = DoubleArray(ELEMENT_COUNT)
		for (y in 0..ROW_COUNT - 1) {
			for (x in 0..COLUMN_COUNT - 1) {
				var value = 0.0
				for (i in 0..ROW_COUNT - 1) {
					value += get(y, i) * b[i, x]
				}
				a[getIndex(y, x)] = value
			}
		}
		return Matrix3(a)
	}
	
	fun transpose(): Matrix3 {
		return Matrix3(
				m[0], m[3], m[6],
				m[1], m[4], m[7],
				m[2], m[5], m[8])
	}
	
	fun determinant(): Double {
		return m[0] * m[4] * m[8] +
				m[1] * m[5] * m[6] +
				m[2] * m[3] * m[7] -
				m[2] * m[4] * m[6] -
				m[1] * m[3] * m[8] -
				m[0] * m[5] * m[7]
	}
	
	fun invert(): Matrix3 {
		val n = Matrix3()
		val inv = 1.0 / determinant()
		
		n.m[0] = inv * (m[4] * m[8] - m[5] * m[7])
		n.m[1] = -inv * (m[1] * m[8] - m[2] * m[7])
		n.m[2] = inv * (m[1] * m[5] - m[2] * m[4])
		n.m[3] = -inv * (m[3] * m[8] - m[5] * m[6])
		n.m[4] = inv * (m[0] * m[8] - m[2] * m[6])
		n.m[5] = -inv * (m[0] * m[5] - m[2] * m[3])
		n.m[6] = inv * (m[3] * m[7] - m[4] * m[6])
		n.m[7] = -inv * (m[0] * m[7] - m[1] * m[6])
		n.m[8] = inv * (m[0] * m[4] - m[1] * m[3])
		return n
	}
	
	operator fun times(v: Vector3) = transform(v)
	fun transform(v: Vector3): Vector3 {
		val x = v.x * m[0] + v.y * m[3] + v.z * m[6]
		val y = v.x * m[1] + v.y * m[4] + v.z * m[7]
		val z = v.x * m[2] + v.y * m[5] + v.z * m[8]
		return Vector3(x, y, z)
	}
	
	fun transformNormal(v: Vector3): Vector3 {
		val fLength = v.length()
		if (fLength == 0.0) return v
		val mTransform = invert().transpose()
		val transformed = Vector3(
				v.x * mTransform.m[0] + v.y * mTransform.m[3] + v.z * mTransform.m[6],
				v.x * mTransform.m[1] + v.y * mTransform.m[4] + v.z * mTransform.m[7],
				v.x * mTransform.m[2] + v.y * mTransform.m[5] + v.z * mTransform.m[8])
		return transformed.normalize() * fLength
	}
	
	
	override fun toString(): String {
		val desiredLen = 10
		val sep = " "
		var s = "[" + sline(0, sep, desiredLen) + StringUtil.NEWLINE
		s += " " + sline(3, sep, desiredLen) + StringUtil.NEWLINE
		s += " " + sline(6, sep, desiredLen) + "]"
		return s
	}
	
	private fun sline(startIndex: Int, sep: String, desiredLen: Int): String {
		var s = ralign(m[startIndex], desiredLen)
		for (i in 1..COLUMN_COUNT - 1) {
			s += sep + ralign(m[startIndex + i], desiredLen)
		}
		return s
	}
	
	private fun ralign(number: Double, desiredLen: Int): String {
		var s = "" + number
		while (s.length < desiredLen) {
			s += " "
		}
		return s
	}
	
	fun data(): FloatArray {
		return floatArrayOf(m[0].toFloat(), m[1].toFloat(), m[2].toFloat(), m[3].toFloat(), m[4].toFloat(), m[5].toFloat(), m[6].toFloat(), m[7].toFloat(), m[8].toFloat())
	}
	
	companion object {
		
		fun identity(): Matrix3 {
			return Matrix3(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0)
		}
		
		fun zero(): Matrix3 {
			return Matrix3(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
		}
		
		private val ROW_COUNT = 3
		private val COLUMN_COUNT = 3
		private val ELEMENT_COUNT = ROW_COUNT * COLUMN_COUNT
	}
}
