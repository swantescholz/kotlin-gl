package game

data class PlayerStatus(var vitality: Int = 1, var finance: Int = 1) {
	fun setTo(o: PlayerStatus) {
		this.vitality = o.vitality
		this.finance = o.finance
	}
	val isDead: Boolean
		get() = vitality <= 0 || finance <= 0
	
	override fun toString(): String = "V: $vitality, F: $finance"
	
	fun reset() {
		vitality = 1
		finance = 1
	}
}