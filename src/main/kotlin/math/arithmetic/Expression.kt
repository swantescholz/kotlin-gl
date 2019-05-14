package math.arithmetic

import util.extensions.nearlyInt
import util.extensions.removeLast
import util.extensions.round
import java.util.*

interface ExpressionNode {
	fun apply(al: ArrayList<Double?>)
}

class ConstantNode(number: Int) : ExpressionNode {
	val number = number.toDouble()
	override fun apply(al: ArrayList<Double?>) {
		al.add(number)
	}
}

open class BiFunctionNode(val bifunction: (Double, Double) -> Double?) : ExpressionNode {
	override fun apply(al: ArrayList<Double?>) {
		if (al.size < 2)
			throw IllegalAccessException("stupid")
		val b = al.get(al.size - 1)
		val a = al.get(al.size - 2)
		al.removeAt(al.size - 1)
		al.removeAt(al.size - 1)
		var res: Double? = null
		if (a != null && b != null)
			res = bifunction(a, b)
		al.add(res)
	}
	
	object Add : BiFunctionNode({ a, b -> a + b })
	
	object Sub : BiFunctionNode({ a, b -> a - b })
	
	object Mul : BiFunctionNode({ a, b -> a * b })
	
	object Div : BiFunctionNode({ a, b -> if (b == 0.0) null else a / b })
}

class Expression() : ArrayList<ExpressionNode>() {
	constructor(expression: Expression) : this() {
		addAll(expression)
	}
	
	override fun toString(): String {
		return super.toString()
	}
	
	fun evaluate(): Int? {
		val al = ArrayList<Double?>()
		for (it in this) {
			it.apply(al)
			if (al.last() == null)
				return null
		}
		if (al[0]?.nearlyInt() ?: false)
			return al[0]?.round()?.toInt()
		return null
	}
}

fun _r_createArithmeticExpressions(expressionStart: Expression, numbers: HashSet<ExpressionNode>,
                                   operators: HashSet<ExpressionNode>, moreNumbers: Int): ArrayList<Expression> {
	val res = ArrayList<Expression>()
	if (moreNumbers > 1) {
		for (op in operators) {
			expressionStart.add(op)
			res.addAll(_r_createArithmeticExpressions(expressionStart, numbers, operators, moreNumbers - 1))
			expressionStart.removeLast()
		}
	} else if (numbers.isEmpty()) {
		return arrayListOf(Expression(expressionStart))
	}
	val numbersNew = HashSet(numbers)
	for (number in numbers) {
		expressionStart.add(number)
		numbersNew.remove(number)
		res.addAll(_r_createArithmeticExpressions(expressionStart, numbersNew, operators, moreNumbers + 1))
		numbersNew.add(number)
		expressionStart.removeLast()
	}
	return res
}

fun createArithmeticExpressions(numbers: HashSet<ConstantNode>, operators: HashSet<BiFunctionNode>): ArrayList<Expression> {
	val n = HashSet<ExpressionNode>(numbers)
	val o = HashSet<ExpressionNode>(operators)
	return _r_createArithmeticExpressions(Expression(), n, o, 0)
}
