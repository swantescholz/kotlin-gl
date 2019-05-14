package gl.gfx.model


class Model(private val mesh: Mesh) : RenderableObject() {
	
	override fun draw() {
		mesh.render()
	}
	
}
