package math.linearalgebra

import gl.util.StringUtil

class Matrix : VectorSpace<Matrix> {
	private val m = DoubleArray(ELEMENT_COUNT)
	
	constructor() {
		
	}
	
	constructor(m: DoubleArray) {
		System.arraycopy(m, 0, this.m, 0, ELEMENT_COUNT)
	}
	
	constructor(
			m00: Double, m01: Double, m02: Double, m03: Double,
			m10: Double, m11: Double, m12: Double, m13: Double,
			m20: Double, m21: Double, m22: Double, m23: Double,
			m30: Double, m31: Double, m32: Double, m33: Double) {
		m[0] = m00
		m[1] = m01
		m[2] = m02
		m[3] = m03
		m[4] = m10
		m[5] = m11
		m[6] = m12
		m[7] = m13
		m[8] = m20
		m[9] = m21
		m[10] = m22
		m[11] = m23
		m[12] = m30
		m[13] = m31
		m[14] = m32
		m[15] = m33
	}
	
	fun copy(): Matrix {
		val n = Matrix()
		System.arraycopy(m, 0, n.m, 0, ELEMENT_COUNT)
		return n
	}
	
	override operator fun unaryMinus(): Matrix {
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
	fun toMatrix3(): Matrix3 {
		return Matrix3(
				m[0], m[1], m[2],
				m[4], m[5], m[6],
				m[8], m[9], m[10])
	}
	
	override operator fun plus(o: Matrix): Matrix {
		val n = copy()
		for (i in 0..ELEMENT_COUNT - 1) {
			n.m[i] += o.m[i]
		}
		return n
	}
	
	override operator fun minus(o: Matrix): Matrix {
		val n = copy()
		for (i in 0..ELEMENT_COUNT - 1) {
			n.m[i] -= o.m[i]
		}
		return n
	}
	
	override operator fun times(o: Double): Matrix {
		val n = copy()
		for (i in 0..ELEMENT_COUNT - 1) {
			n.m[i] *= o
		}
		return n
	}
	
	fun equal(n: Matrix): Boolean {
		return m[0] == n.m[0] && m[1] == n.m[1] && m[2] == n.m[2] && m[3] == n.m[3] &&
				m[4] == n.m[4] && m[5] == n.m[5] && m[6] == n.m[6] && m[7] == n.m[7] &&
				m[8] == n.m[8] && m[9] == n.m[9] && m[10] == n.m[10] && m[11] == n.m[11] &&
				m[12] == n.m[12] && m[13] == n.m[13] && m[14] == n.m[14] && m[15] == n.m[15]
	}
	
	override fun almostEqual(o: Matrix): Boolean {
		for (i in 0..ELEMENT_COUNT - 1) {
			if (!MathUtil.almostEqual(m[i], o.m[i]))
				return false
		}
		return true
	}
	
	operator fun times(b: Matrix): Matrix {
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
		return Matrix(a)
	}
	
	fun transpose(): Matrix {
		return Matrix(
				m[0], m[4], m[8], m[12],
				m[1], m[5], m[9], m[13],
				m[2], m[6], m[10], m[14],
				m[3], m[7], m[11], m[15])
	}
	
	fun determinant(): Double {
		return m[0] * (m[5] * m[10] - m[6] * m[9]) - m[1] * (m[4] * m[10] - m[6] * m[8]) + m[2] * (m[4] * m[9] - m[5] * m[8])
	}
	
	fun invert(): Matrix {
		val n = Matrix()
		val fInvDet = 1.0 / determinant()
		
		n.m[0] = fInvDet * (m[5] * m[10] - m[6] * m[9])
		n.m[1] = -fInvDet * (m[1] * m[10] - m[2] * m[9])
		n.m[2] = fInvDet * (m[1] * m[6] - m[2] * m[5])
		n.m[3] = 0.0
		n.m[4] = -fInvDet * (m[4] * m[10] - m[6] * m[8])
		n.m[5] = fInvDet * (m[0] * m[10] - m[2] * m[8])
		n.m[6] = -fInvDet * (m[0] * m[6] - m[2] * m[4])
		n.m[7] = 0.0
		n.m[8] = fInvDet * (m[4] * m[9] - m[5] * m[8])
		n.m[9] = -fInvDet * (m[0] * m[9] - m[1] * m[8])
		n.m[10] = fInvDet * (m[0] * m[5] - m[1] * m[4])
		n.m[11] = 0.0
		n.m[12] = -(m[12] * n.m[0] + m[13] * n.m[4] + m[14] * m[8])
		n.m[13] = -(m[12] * n.m[1] + m[13] * n.m[5] + m[14] * m[9])
		n.m[14] = -(m[12] * n.m[2] + m[13] * n.m[6] + m[14] * m[10])
		n.m[15] = 1.0
		return n
	}
	
	operator fun times(v: Vector3) = transform(v)
	operator fun times(v: Vector4) = transform(v)
	fun transform(v: Vector3): Vector3 {
		val x = v.x * m[0] + v.y * m[4] + v.z * m[8] + m[12]
		val y = v.x * m[1] + v.y * m[5] + v.z * m[9] + m[13]
		val z = v.x * m[2] + v.y * m[6] + v.z * m[10] + m[14]
		val w = v.x * m[3] + v.y * m[7] + v.z * m[11] + m[15]
		if (w != 1.0) {
			return Vector3(x, y, z).div(w)
		}
		return Vector3(x, y, z)
	}
	
	fun transform(v: Vector4): Vector4 {
		val x = v.x * m[0] + v.y * m[4] + v.z * m[8] + v.w * m[12]
		val y = v.x * m[1] + v.y * m[5] + v.z * m[9] + v.w * m[13]
		val z = v.x * m[2] + v.y * m[6] + v.z * m[10] + v.w * m[14]
		val w = v.x * m[3] + v.y * m[7] + v.z * m[11] + v.w * m[15]
		return Vector4(x, y, z, w)
	}
	
	fun transformNormal(v: Vector3): Vector3 {
		val fLength = v.length()
		if (fLength == 0.0) return v
		val mTransform = invert().transpose()
		val transformed = Vector3(
				v.x * mTransform.m[0] + v.y * mTransform.m[4] + v.z * mTransform.m[8],
				v.x * mTransform.m[1] + v.y * mTransform.m[5] + v.z * mTransform.m[9],
				v.x * mTransform.m[2] + v.y * mTransform.m[6] + v.z * mTransform.m[10])
		return transformed.normalize() * fLength
	}
	
	override fun toString(): String {
		val desiredLen = 10
		val sep = " "
		var s = "[" + sline(0, sep, desiredLen) + StringUtil.NEWLINE
		s += " " + sline(4, sep, desiredLen) + StringUtil.NEWLINE
		s += " " + sline(8, sep, desiredLen) + StringUtil.NEWLINE
		s += " " + sline(12, sep, desiredLen) + "]"
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
		return floatArrayOf(m[0].toFloat(), m[1].toFloat(), m[2].toFloat(), m[3].toFloat(), m[4].toFloat(), m[5].toFloat(), m[6].toFloat(), m[7].toFloat(), m[8].toFloat(), m[9].toFloat(), m[10].toFloat(), m[11].toFloat(), m[12].toFloat(), m[13].toFloat(), m[14].toFloat(), m[15].toFloat())
	}
	
	companion object {
		
		fun identity(): Matrix {
			return Matrix(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)
		}
		
		fun zero(): Matrix {
			return Matrix(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
		}
		
		private val ROW_COUNT = 4
		private val COLUMN_COUNT = 4
		private val ELEMENT_COUNT = ROW_COUNT * COLUMN_COUNT
		
		fun translation(v: Vector3): Matrix {
			return Matrix(
					1.0, 0.0, 0.0, 0.0,
					0.0, 1.0, 0.0, 0.0,
					0.0, 0.0, 1.0, 0.0,
					v.x, v.y, v.z, 1.0)
		}
		
		fun rotationFromAxes(xAxis: Vector3, yAxis: Vector3, zAxis: Vector3): Matrix {
			return Matrix(
					xAxis.x, xAxis.y, xAxis.z, 0.0,
					yAxis.x, yAxis.y, yAxis.z, 0.0,
					zAxis.x, zAxis.y, zAxis.z, 0.0,
					0.0, 0.0, 0.0, 1.0)
		}
		
		fun rotation(axis: Vector3, angle: Double): Matrix {
			val x = axis.normalize()
			val fSin = Math.sin(-angle)
			val fCos = Math.cos(-angle)
			val fOneMinusCos = 1.0 - fCos
			
			return Matrix(
					x.x * x.x * fOneMinusCos + fCos,
					x.x * x.y * fOneMinusCos - x.z * fSin,
					x.x * x.z * fOneMinusCos + x.y * fSin,
					0.0,
					x.y * x.x * fOneMinusCos + x.z * fSin,
					x.y * x.y * fOneMinusCos + fCos,
					x.y * x.z * fOneMinusCos - x.x * fSin,
					0.0,
					x.z * x.x * fOneMinusCos - x.y * fSin,
					x.z * x.y * fOneMinusCos + x.x * fSin,
					x.z * x.z * fOneMinusCos + fCos,
					0.0,
					0.0, 0.0, 0.0, 1.0)
		}
		
		fun scaling(s: Double): Matrix {
			return Matrix.scaling(Vector3(s, s, s))
		}
		
		fun scaling(v: Vector3): Matrix {
			return Matrix(
					v.x, 0.0, 0.0, 0.0,
					0.0, v.y, 0.0, 0.0,
					0.0, 0.0, v.z, 0.0,
					0.0, 0.0, 0.0, 1.0)
		}
		
		fun projection(fov: Double, aspectRatio: Double, nearPlane: Double, farPlane: Double): Matrix {
			val dxInv = 1.0 / (2.0 * nearPlane * Math.tan(fov * 0.5))
			val dyInv = dxInv * aspectRatio
			val dzInv = 1.0 / (farPlane - nearPlane)
			val x = 2.0 * nearPlane
			val c = -(nearPlane + farPlane) * dzInv
			val d = -nearPlane * farPlane * dzInv
			return Matrix(x * dxInv, 0.0, 0.0, 0.0, 0.0, x * dyInv, 0.0, 0.0, 0.0, 0.0, c, -1.0, 0.0, 0.0, d, 0.0)
		}
		
		fun ortho(left: Double, right: Double, bottom: Double, top: Double, near: Double, far: Double): Matrix {
			val a = 2 / (right - left)
			val b = 2 / (top - bottom)
			val c = -2 / (far - near)
			return Matrix(
					a, 0.0, 0.0, -(right + left) / (right - left),
					0.0, b, 0.0, -(top + bottom) / (top - bottom),
					0.0, 0.0, c, -(far + near) / (far - near),
					0.0, 0.0, 0.0, 1.0)
		}
		
		fun camera(pos: Vector3, dir: Vector3, up: Vector3): Matrix {
			val zAxis = -dir.normalize()
			val xAxis = up.cross(zAxis).normalize()
			val yAxis = zAxis.cross(xAxis).normalize()
			
			val negPos = -pos
			val result = Companion.translation(negPos)
			return result.times(Matrix(
					xAxis.x, yAxis.x, zAxis.x, 0.0,
					xAxis.y, yAxis.y, zAxis.y, 0.0,
					xAxis.z, yAxis.z, zAxis.z, 0.0,
					0.0, 0.0, 0.0, 1.0))
		}
		
		fun shear(amount: Double): Matrix {
			return Matrix(
					1.0, 0.0, 0.0, 0.0,
					0.0, 1.0, 0.0, 0.0,
					amount, amount, 1.0, 0.0,
					0.0, 0.0, 0.0, 1.0)
		}
		
		
	}
	
}
