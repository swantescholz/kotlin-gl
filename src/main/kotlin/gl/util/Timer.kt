package gl.util

import java.util.*


class Timer(private val duration: Double) {
	var time = 0.0
		private set
	var isExpired = false
		private set
	
	init {
		timerList.add(this)
	}
	
	private fun update(elapsed: Double) {
		this.time += elapsed
		checkExpiry()
	}
	
	private fun checkExpiry() {
		if (this.time >= duration) {
			isExpired = true
		}
	}
	
	fun reset() {
		time = 0.0
		isExpired = false
		timerList.add(this)
	}
	
	companion object {
		
		
		private val timerList = ArrayList<Timer>()
		
		fun updateAll(elapsed: Double) {
			val it = timerList.iterator()
			while (it.hasNext()) {
				val timer = it.next()
				timer.update(elapsed)
				if (timer.isExpired) {
					it.remove()
				}
			}
		}
	}
}
