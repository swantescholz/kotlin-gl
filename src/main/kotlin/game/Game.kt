package game

import gl.JoglMain
import gl.gfx.GlUtil
import gl.gfx.light.PositionalLight
import gl.gfx.shader.CompilationFailedException
import gl.gfx.shader.Program
import gl.gfx.shader.ProgramManager
import gl.gfx.shader.ShaderManager
import gl.gfx.texture.TextureManager
import gl.input.Keyboard
import gl.input.Mouse
import gl.math.Camera
import gl.math.Color
import gl.math.Material
import gl.util.FrameRateComputer
import gl.util.StringUtil
import gl.util.Timer
import math.linearalgebra.Vector3
import util.Config
import util.extensions.clamp
import util.extensions.int
import util.extensions.pow
import util.extensions.println
import util.log.Log
import util.log.LogLevel
import java.awt.event.KeyEvent
import java.io.File
import java.io.FileNotFoundException

class Game(val joglMain: JoglMain) {
	
	companion object {
		fun mymain() {
			Log.addListener(System.out, LogLevel.INFO)
			try {
				Log.addOutputFile(File("logs/${StringUtil.currentTimestamp()}.txt"), LogLevel.DEBUG)
			} catch (e: FileNotFoundException) {
				Log.errorAndThrow(e)
			}
			Log.info("starting Game")
			var game: Game? = null
			JoglMain({ joglMain ->
				game = Game(joglMain)
			}, {
				if (game != null)
					game!!.mainLoop()
			})
		}
		
		val maxCameraZ = 400.0
		val minCameraZ = 2.0
		val defaultCameraZ = 20.0
	}
	
	private fun mainLoop() {
		update()
		render()
	}
	
	val keyboard: Keyboard
	val mouse: Mouse
	val camera = Camera()
	var elapsed: Double = 0.toDouble()
	val fpsComputer = FrameRateComputer()
	val timerA = Timer(4.0)
	var light: PositionalLight = PositionalLight()
	val shaderManager = ShaderManager.instance
	val programManager = ProgramManager.instance
	val textureManager = TextureManager.instance
	var testProgram: Program = ProgramManager.DEFAULT
	var functionProgram: Program = ProgramManager.DEFAULT
	var colorProgram: Program = ProgramManager.DEFAULT
	var textureProgram: Program = ProgramManager.DEFAULT
	var cameraZ = defaultCameraZ
	var lockCameraToCenter = false
	var level = Level(File("res/level/level1"))
	var numberOfShownRooms = level.maxNumberOfRooms
	var currentLevelId = Config.game["number.of.levels"].int
	
	init {
		keyboard = joglMain.keyboard
		mouse = joglMain.mouse
		Log.info("init Demo, ${joglMain.canvas.width}, ${joglMain.canvas.height} canvas")
		light = PositionalLight()
		light.ambient = Color.WHITE
		light.diffuse = Color.WHITE
		light.specular = Color.WHITE
		light.position = Vector3(-2.0, 4.0, 4.0)
		
		camera.upVector = Vector3.Y
		camera.position = Vector3(0.0, 0.0, cameraZ)
		camera.direction = Vector3.NZ
		
		initResources()
		
		reset()
		GlUtil.setClearColor(Color.BLACK)
	}
	
	private fun initResources() {
		listOf("pcute/key", "pcute/character-horn-girl", "pcute/heart",
				"pcute/selector", "pcute/gem-green", "pcute/door-tall-closed", "pcute/door-tall-open",
				"lava", "dragonb", "sword").forEach {
			textureManager.load(it, transparentBorderSize = 5)
		}
	}
	
	private fun setupFrame() {
		camera.apply()
		light.shine()
	}
	
	private fun reset() {
		Log.info("reloading")
		Config.game.reload()
		level = Level(File("res/level/level$currentLevelId"), numberOfShownRooms)
		joglMain.title = Config.game["windowTitle"]
		joglMain.setSize(Config.game["windowWidth"].int, Config.game["windowHeight"].int)
		shaderManager.reloadHeaders()
		try {
			shaderManager.loadVert("v.test", "test")
			shaderManager.loadFrag("f.test", "test")
			shaderManager.loadVert("v.function", "function")
			shaderManager.loadFrag("f.function", "function")
			shaderManager.loadVert("v.simplePosition", "simplePosition")
			shaderManager.loadFrag("f.simpleDiffuseColor", "simpleDiffuseColor")
			shaderManager.loadVert("v.simpleFragCoords", "simpleFragCoords")
			shaderManager.loadFrag("f.simpleTexture", "simpleTexture")
		} catch (e: CompilationFailedException) {
			Log.warn("shader compilation failed.")
		}
		
		testProgram = programManager.linkProgram("test", "v.test", "f.test")
		functionProgram = programManager.linkProgram("function", "v.function", "f.function")
		colorProgram = programManager.linkProgram("color", "v.simplePosition", "f.simpleDiffuseColor")
		textureProgram = programManager.linkProgram("texture", "v.simpleFragCoords", "f.simpleTexture")
	}
	
	fun render() {
		testProgram.use()
		setupFrame()
		Material.MELLOW.use()
		level.render()
		Hud.renderAllCorners()
	}
	
	fun update() {
		fpsComputer.update()
		elapsed = fpsComputer.elapsed
		Timer.updateAll(elapsed)
		if (timerA.isExpired) {
			timerA.reset()
			fpsComputer.averageFps.println()
		}
		level.update(keyboard)
		if (lockCameraToCenter) {
			camera.position = level.centerCoordinates.to3(z = cameraZ)
		} else {
			camera.position = camera.position.interpolateExponentially(
					level.realCurrentPlayerPosition.to3(cameraZ), 0.4, elapsed)
		}
		light.position = camera.position + Vector3(0, 5, 1)
		processInput()
	}
	
	private fun processInput() {
		if (keyboard.wasPressed(KeyEvent.VK_I) && numberOfShownRooms > 1) {
			numberOfShownRooms--
			reset()
		} else if (keyboard.wasPressed(KeyEvent.VK_O) && numberOfShownRooms < level.maxNumberOfRooms) {
			numberOfShownRooms++
			reset()
		}
		if (keyboard.wasPressed(KeyEvent.VK_K) && currentLevelId > 1) {
			currentLevelId--
			reset()
		} else if (keyboard.wasPressed(KeyEvent.VK_L) && currentLevelId < Config.game["number.of.levels"].int) {
			currentLevelId++
			reset()
		}
		if (keyboard.wasPressed(KeyEvent.VK_F))
			lockCameraToCenter = !lockCameraToCenter
		if (keyboard.wasPressed(KeyEvent.VK_U))
			GlUtil.toggleProjectionMode()
		if (keyboard.wasPressed(KeyEvent.VK_T))
			GlUtil.toggleWireframeMode()
		if (keyboard.wasPressed(KeyEvent.VK_P))
			GfxUtil.saveScreenshot()
		if (keyboard.wasPressed(KeyEvent.VK_R))
			reset()
		if (keyboard.isDown(KeyEvent.VK_X))
			cameraZ *= 2.0.pow(elapsed)
		else if (keyboard.isDown(KeyEvent.VK_C))
			cameraZ *= 0.5.pow(elapsed)
		cameraZ = cameraZ.clamp(minCameraZ, maxCameraZ)
	}
	
}


