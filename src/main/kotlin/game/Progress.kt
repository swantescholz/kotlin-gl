package game

import util.Config
import util.astLess
import util.astTrue
import util.extensions.int
import util.extensions.string
import java.io.File

object Progress {
	private val configSettings = Config(File("res/progress.txt"))
	private val unlockedLevelsKey = "number.of.unlocked.levels"
	private val levelProgressKey = "solved.rooms.of.level"
	
	private fun updateProgressFile() {
		configSettings.writeToFile()
	}
	
	// levelId starts at 1
	fun getLevelProgress(levelId: Int): Int {
		val key = "$levelProgressKey$levelId"
		if (key !in configSettings)
			return -1
		return configSettings[key].toInt()
	}
	
	fun increaseLevelProgress(levelId: Int) {
		val key = "$levelProgressKey$levelId"
		astTrue(key in configSettings)
		configSettings[key] = (configSettings[key].int + 1).string
		updateProgressFile()
	}
	
	fun unlockNextLevel() {
		val numCurrentlyUnlockedLevels = configSettings[unlockedLevelsKey].int
		astLess(numCurrentlyUnlockedLevels, Config.game["number.of.levels"].int)
		configSettings[unlockedLevelsKey] = (numCurrentlyUnlockedLevels + 1).string
		configSettings["$unlockedLevelsKey${numCurrentlyUnlockedLevels + 1}"] = "0"
		updateProgressFile()
	}
}