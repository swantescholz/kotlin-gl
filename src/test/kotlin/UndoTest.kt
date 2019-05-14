import game.UndoManager
import org.junit.Test
import util.astEqual

class UndoTest {
	
	@Test
	fun test() {
		val u = UndoManager()
		var x = 1
		u.startPotentialActionGroup()
		u.addActionToGroup(action = { x++ }, undoAction = { x-- })
		u.endPotentialActionGroup()
		astEqual(x, 2)
		u.undo()
		astEqual(x, 1)
		u.redo()
		astEqual(x, 2)
		u.startPotentialActionGroup()
		u.addActionToGroup(action = { x = 7 }, undoAction = { x = 2 })
		astEqual(x, 7)
		u.addActionToGroup(action = { x *= 2 }, undoAction = { x /= 2 })
		astEqual(x, 14)
		u.endPotentialActionGroup()
		astEqual(u.countPossibleUndos(), 2)
		astEqual(u.countPossibleRedos(), 0)
		u.undo()
		astEqual(u.countPossibleUndos(), 1)
		astEqual(u.countPossibleRedos(), 1)
		astEqual(x, 2)
		u.undo()
		astEqual(x, 1)
		astEqual(u.countPossibleRedos(), 2)
		u.startPotentialActionGroup()
		u.addActionToGroup(action = { x++ }, undoAction = { x-- })
		u.endPotentialActionGroup()
		astEqual(u.countPossibleUndos(), 1)
		astEqual(u.countPossibleRedos(), 0)
		u.clearHistory()
		astEqual(u.countPossibleUndos(), 0)
	}
}

