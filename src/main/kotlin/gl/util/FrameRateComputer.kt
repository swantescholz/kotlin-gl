package gl.util

import java.util.*

class FrameRateComputer @JvmOverloads constructor(private val timeoutInterval: Double = 5.0) {
	private var lastTime: Long = -1
	var elapsed = 0.0
		private set
	
	private inner class Measurement(val time: Long, val elapsed: Double)
	
	private val measurements = LinkedList<Measurement>()
	
	val averageFps: Double
		get() {
			if (measurements.isEmpty())
				return 0.0
			var sum = 0.0
			for (measurement in measurements) {
				sum += measurement.elapsed
			}
			return measurements.size / sum
		}
	
	fun update() {
		val currentTime = System.nanoTime()
		if (lastTime >= 0) {
			val elapsed = (currentTime - lastTime) / 1.0e9
			val measurement = Measurement(currentTime, elapsed)
			measurements.add(measurement)
			this.elapsed = elapsed
		}
		lastTime = currentTime
		removeOldMeasurements()
	}
	
	private fun removeOldMeasurements() {
		while (!measurements.isEmpty()) {
			val measurement = measurements.peek()
			val diff = (lastTime - measurement.time) / 1.0e9
			if (diff > timeoutInterval) {
				measurements.remove()
			} else {
				break
			}
		}
	}
	
	
}
