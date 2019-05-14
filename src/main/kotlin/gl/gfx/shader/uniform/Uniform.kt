package gl.gfx.shader.uniform

import gl.gfx.gl
import gl.gfx.light.ILight
import gl.gfx.texture.Texture
import gl.math.Camera
import gl.math.Color
import gl.math.Material
import math.linearalgebra.*
import util.log.Log
import javax.media.opengl.GL

// update uniform simply by assigning to *value*
class Uniform<T>(private val name: String) : IUniform {
	private val buffer = intArrayOf(1)
	private var currentProgram: Int = 0
	
	private val simpleLocation: Int
		get() = getLocation(name)
	
	private fun getLocation(fullName: String): Int {
		val location = gl().glGetUniformLocation(currentProgram, fullName)
		return location
	}
	
	var value: T? = null
		set(newValue) = {
			field = newValue
			applyToCurrentShader()
		}()
	
	
	// applies uniform to current shader-program
	override fun applyToCurrentShader() {
		val gl = gl()
		gl.glGetIntegerv(GL.GL_CURRENT_PROGRAM, buffer, 0)
		this.currentProgram = buffer[0]
		if (value == null) {
			return
		} else if (value is Int) {
			gl.glUniform1i(simpleLocation, (value as Int?)!!)
		} else if (value is Double) {
			val data = (value as Double?)!!
			gl.glUniform1f(simpleLocation, data.toFloat())
		} else if (value is Vector2) {
			val data = value as Vector2?
			gl.glUniform2f(simpleLocation, data!!.x.toFloat(), data.y.toFloat())
		} else if (value is Vector3) {
			uniformVector3(simpleLocation, value as Vector3)
		} else if (value is Color) {
			uniformColor(simpleLocation, value as Color)
		} else if (value is Quaternion) {
			val data = value as Quaternion?
			gl.glUniform4f(simpleLocation, data!!.w.toFloat(), data.x.toFloat(), data.y.toFloat(), data.z.toFloat())
		} else if (value is Matrix3) {
			val matrix = value as Matrix3?
			gl.glUniformMatrix3fv(simpleLocation, 1, false, matrix!!.data(), 0)
		} else if (value is Matrix) {
			val matrix = value as Matrix?
			gl.glUniformMatrix4fv(simpleLocation, 1, false, matrix!!.data(), 0)
		} else if (value is Camera) {
			val camera = value as Camera
			uniformVector3(getLocation(name + ".pos"), camera.position)
			uniformVector3(getLocation(name + ".dir"), camera.direction)
			uniformVector3(getLocation(name + ".up"), camera.upVector)
		} else if (value is Material) {
			val material = value as Material
			uniformColor(getLocation(name + ".ambient"), material.ambient)
			uniformColor(getLocation(name + ".diffuse"), material.diffuse)
			uniformColor(getLocation(name + ".specular"), material.specular)
			gl.glUniform1f(getLocation(name + ".shininess"), material.shininess.toFloat())
		} else if (value is ILight) {
			val light = value as ILight
			uniformColor(getLocation(name + ".ambient"), light.ambient)
			uniformColor(getLocation(name + ".diffuse"), light.diffuse)
			uniformColor(getLocation(name + ".specular"), light.specular)
			uniformVector3(getLocation(name + ".position"), light.position)
			uniformVector3(getLocation(name + ".spotDirection"), light.spotDirection)
			gl.glUniform1f(getLocation(name + ".spotCutoff"), light.spotCutoff.toFloat())
			gl.glUniform1f(getLocation(name + ".spotCosCutoff"), light.spotCosCutoff.toFloat())
			gl.glUniform1f(getLocation(name + ".spotExponent"), light.spotExponent.toFloat())
			gl.glUniform1f(getLocation(name + ".constantAttenuation"), light.constantAttenuation.toFloat())
			gl.glUniform1f(getLocation(name + ".linearAttenuation"), light.linearAttenuation.toFloat())
			gl.glUniform1f(getLocation(name + ".quadraticAttenuation"), light.quadraticAttenuation.toFloat())
			uniformBoolean(getLocation(name + ".enabled"), light.isEnabled)
			uniformBoolean(getLocation(name + ".positional"), light.isPositional)
		} else if (value is Texture) {
			val texture = value as Texture
			val layerIndex = Integer.parseInt(name.substring(9, name.length - 1))
			gl.glUniform1i(simpleLocation, layerIndex)
			gl.glActiveTexture(GL.GL_TEXTURE0 + layerIndex)
			texture.bind()
		} else {
			Log.errorAndExit("Uniform value '$value' not supported.")
		}
	}
	
	private fun uniformVector3(location: Int, v: Vector3) {
		gl().glUniform3f(location, v.x.toFloat(), v.y.toFloat(), v.z.toFloat())
	}
	
	private fun uniformColor(location: Int, color: Color) {
		gl().glUniform4f(location, color.r.toFloat(), color.g.toFloat(), color.b.toFloat(), color.a.toFloat())
	}
	
	private fun uniformBoolean(location: Int, value: Boolean) {
		gl().glUniform1i(location, if (value) 1 else 0)
	}
}
