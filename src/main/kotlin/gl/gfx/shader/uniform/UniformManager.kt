package gl.gfx.shader.uniform

import gl.gfx.light.Light
import gl.gfx.texture.Texture
import gl.math.Camera
import gl.math.Color
import gl.math.Material
import math.linearalgebra.Matrix
import math.linearalgebra.Matrix3
import java.util.*

// default uniforms can be set before or after glUseProgram, special uniforms only after glUseProgram
object UniformManager {
	
	private val defaultUniforms = ArrayList<IUniform>()
	private val modelMatrix = Uniform<Matrix>("uModelMatrix")
	private val viewMatrix = Uniform<Matrix>("uViewMatrix")
	private val projectionMatrix = Uniform<Matrix>("uProjectionMatrix")
	private val modelViewMatrix = Uniform<Matrix>("uModelViewMatrix")
	private val viewProjectionMatrix = Uniform<Matrix>("uViewProjectionMatrix")
	private val modelViewProjectionMatrix = Uniform<Matrix>("uModelViewProjectionMatrix")
	private val normalMatrix = Uniform<Matrix3>("uNormalMatrix")
	val elapsedTime = Uniform<Double>("uElapsedTime")
	val timeSinceInit = Uniform<Double>("uTimeSinceInit")
	val camera = Uniform<Camera>("uCamera")
	val lightModelAmbient = Uniform<Color>("uLightModelAmbient")
	val material = Uniform<Material>("uMaterial")
	val lights = ArrayList<Uniform<Light>>()
	val textures = ArrayList<Uniform<Texture>>()
	
	init {
		setDefaultValues()
		defaultUniforms.addAll(Arrays.asList(modelMatrix, viewMatrix, projectionMatrix,
				normalMatrix, modelViewMatrix, viewProjectionMatrix, modelViewProjectionMatrix,
				elapsedTime, timeSinceInit, camera, lightModelAmbient, material))
		defaultUniforms.addAll(lights)
		defaultUniforms.addAll(textures)
	}
	
	private fun setDefaultValues() {
		for (i in 0..Light.MAX_NUMBER - 1) {
			val lightUniform = Uniform<Light>("uLightSource[$i]")
			lights.add(lightUniform)
		}
		for (i in 0..Texture.MAX_LAYERS - 1) {
			val textureUniform = Uniform<Texture>("uTexture[$i]")
			textures.add(textureUniform)
		}
		val id = Matrix.identity()
		modelMatrix.value = id
		viewMatrix.value = id
		projectionMatrix.value = id
		normalMatrix.value = Matrix3.identity()
		modelViewMatrix.value = id
		viewProjectionMatrix.value = id
		modelViewProjectionMatrix.value = id
		elapsedTime.value = 0.1
		timeSinceInit.value = 0.1
		camera.value = Camera()
		lightModelAmbient.value = Color(.2, .2, .2, 1.0)
	}
	
	fun applyAllDefaultUniformsToCurrentShader() {
		defaultUniforms.forEach { it.applyToCurrentShader() }
	}
	
	fun setModelMatrix(newModelMatrix: Matrix) {
		modelMatrix.value = newModelMatrix
		modelViewMatrix.value = newModelMatrix.times(viewMatrix.value!!)
		modelViewProjectionMatrix.value = modelViewMatrix.value!!.times(projectionMatrix.value!!)
		normalMatrix.value = newModelMatrix.toMatrix3().transpose()
	}
	
	fun setViewMatrix(newViewMatrix: Matrix) {
		viewMatrix.value = newViewMatrix
		modelViewMatrix.value = modelMatrix.value!!.times(newViewMatrix)
		viewProjectionMatrix.value = newViewMatrix.times(projectionMatrix.value!!)
		modelViewProjectionMatrix.value = modelViewMatrix.value!!.times(projectionMatrix.value!!)
	}
	
	fun setProjectionMatrix(newProjectionMatrix: Matrix) {
		projectionMatrix.value = newProjectionMatrix
		viewProjectionMatrix.value = viewMatrix.value!!.times(newProjectionMatrix)
		modelViewProjectionMatrix.value = modelViewMatrix.value!!.times(newProjectionMatrix)
	}
	
	fun getModelMatrix(): Matrix {
		return modelMatrix.value!!
	}
	
	fun getViewMatrix(): Matrix {
		return viewMatrix.value!!
	}
	
	fun getProjectionMatrix(): Matrix {
		return projectionMatrix.value!!
	}
	
	fun getModelViewMatrix(): Matrix {
		return modelViewMatrix.value!!
	}
	
	fun getViewProjectionMatrix(): Matrix {
		return viewProjectionMatrix.value!!
	}
	
	fun getModelViewProjectionMatrix(): Matrix {
		return modelViewProjectionMatrix.value!!
	}
	
	fun setCamera(camera: Camera) {
		this.camera.value = camera
		setViewMatrix(camera.cameraMatrix)
	}
	
	fun applyLight(light: Light) {
		val id = light.id
		val lightUniform = lights[id]
		lightUniform.value = light
	}
	
	fun setTexture(texture: Texture, textureLayerIndex: Int) {
		textures[textureLayerIndex].value = texture
	}
	
}
