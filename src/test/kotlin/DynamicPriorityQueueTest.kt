import org.junit.Assert
import org.junit.Test
import util.datastructures.DynamicPriorityQueue


class DynamicPriorityQueueTest {

	@Test
	fun testQueueWithSamePriorities() {
		val q = DynamicPriorityQueue<Long>(isMaxQueue = true)
		q.add(1, 15.0)
		q.add(2, 6.0)
		q.add(3, 8.0)
		Assert.assertEquals(3, q.size)
		Assert.assertEquals(1, q.poll())
		Assert.assertEquals(2, q.size)
		q.add(1, 8.0)
		Assert.assertEquals(3, q.size)
		q.add(2, 11.0)
		Assert.assertEquals(3, q.size)
		q.remove(3)
		Assert.assertEquals(2, q.size)
		Assert.assertEquals(2, q.poll())
		Assert.assertEquals(1, q.size)
		Assert.assertFalse(q.isEmpty())
		Assert.assertEquals(1, q.poll())
		Assert.assertTrue(q.isEmpty())
	}

}