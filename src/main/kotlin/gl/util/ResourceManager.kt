package gl.util

import util.astTrue
import util.log.Log
import java.util.*

abstract class ResourceManager<R> protected constructor() {
	
	protected val resources: MutableMap<String, R> = HashMap()
	
	protected fun addResource(resourceId: String, resource: R) {
		if (hasResource(resourceId)) {
			Log.warn("Resource '$resourceId' already added!")
		}
		resources.put(resourceId, resource)
	}
	
	protected fun removeResource(resourceId: String) {
		astTrue(hasResource(resourceId), "Resource '$resourceId' to be deleted does not exist")
		resources.remove(resourceId)
	}
	
	protected fun hasResource(resourceId: String): Boolean {
		return resources.containsKey(resourceId)
	}
	
	protected fun getResource(resourceId: String): R {
		astTrue(hasResource(resourceId), "Resource '$resourceId' not found!")
		return resources[resourceId]!!
	}
	
}
