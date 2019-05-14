import util.extensions.min
import util.extensions.swap
import util.extensions.toal
import util.string.pln
import java.util.*

fun <T : Comparable<T>> partition(a: ArrayList<T>, i: Int = 0, j: Int = a.size - 1): Int {
	val pivot = a[j]
	var r = i - 1
	for (k in i..j - 1) {
		if (a[k] <= pivot) {
			r++
			a.swap(r, k)
		}
	}
	a.swap(r + 1, j)
	return r + 1
}

fun main(args: Array<String>) {
	val reps = 1000
	val N = 10000
	var sum = 0.0
	for (i in 1..reps) {
		val nums = Array(N, { it }).toal()
		Collections.shuffle(nums)
		val leftSize = partition(nums)
		val rightSize = N - leftSize
		sum += leftSize.min(rightSize)
	}
	sum /= reps
	pln(sum, sum / N)
}