package game

import util.astFalse
import util.astNotNull
import util.astNull
import java.util.*

class UndoManager {
	
	companion object {
		val game = UndoManager()
	}
	
	private data class Action(val action: () -> Unit, val undoAction: () -> Unit)
	private class ActionGroup {
		private val actions = ArrayList<Action>()
		fun isEmpty() = actions.isEmpty()
		fun addAction(action: Action) = actions.add(action)
		fun execute() = actions.forEach { it.action() }
		fun unexecute() = actions.asReversed().forEach { it.undoAction() }
	}
	
	private val doneActions = Stack<ActionGroup>()
	private val undoneActions = Stack<ActionGroup>()
	private var currentActionGroup: ActionGroup? = null
	
	fun clearHistory() {
		doneActions.clear()
		undoneActions.clear()
	}
	
	fun startPotentialActionGroup() {
		astNull(currentActionGroup, "cannot start another action group! end previous action group first!")
		astFalse(isInUndoOperation, "cannot start action group during undo operation!")
		astFalse(isInRedoOperation, "cannot start action group during redo operation!")
		currentActionGroup = ActionGroup()
	}
	
	fun addActionToGroup(action: () -> Unit, undoAction: () -> Unit) {
		astNotNull(currentActionGroup, "no action group started!")
		currentActionGroup!!.addAction(Action(action, undoAction))
		action()
	}
	
	fun endPotentialActionGroup() {
		astNotNull(currentActionGroup, "no action group started!")
		if (!currentActionGroup!!.isEmpty()) {
			doneActions.add(currentActionGroup)
			undoneActions.clear()
		}
		currentActionGroup = null
	}
	
	private var isInUndoOperation: Boolean = false
	private var isInRedoOperation: Boolean = false
	
	fun undo() {
		astNull(currentActionGroup, "cannot undo while new action group has been started")
		if (doneActions.empty())
			return
		isInUndoOperation = true
		val lastAction = doneActions.pop()
		lastAction.unexecute()
		undoneActions.add(lastAction)
		isInUndoOperation = false
	}
	
	fun redo() {
		astNull(currentActionGroup, "cannot redo while new action group has been started")
		if (undoneActions.empty())
			return
		isInRedoOperation = true
		val lastUndoneAction = undoneActions.pop()
		lastUndoneAction.execute()
		doneActions.push(lastUndoneAction)
		isInRedoOperation = false
	}
	
	fun countPossibleUndos(): Int = doneActions.size
	fun countPossibleRedos(): Int = undoneActions.size
	
}