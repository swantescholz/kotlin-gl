package gl

import game.Hud
import gl.gfx.GlUtil
import gl.gfx.font.Font
import gl.gfx.gl
import gl.gfx.light.PositionalLight
import gl.gfx.model.*
import gl.gfx.shader.CompilationFailedException
import gl.gfx.shader.Program
import gl.gfx.shader.ProgramManager
import gl.gfx.shader.ShaderManager
import gl.gfx.shader.uniform.UniformManager
import gl.gfx.texture.TextureManager
import gl.input.Keyboard
import gl.input.Mouse
import gl.math.*
import gl.util.FrameRateComputer
import gl.util.Slider
import gl.util.StringUtil
import gl.util.Timer
import math.linearalgebra.MathUtil
import math.linearalgebra.Matrix
import math.linearalgebra.Vector2
import math.linearalgebra.Vector3
import util.Config
import util.extensions.string
import util.log.Log
import util.log.LogLevel
import util.random.RANDOM
import java.awt.event.KeyEvent
import java.io.File
import java.io.FileNotFoundException
import javax.media.opengl.GL

class GlDemo(val joglMain: JoglMain) {
	
	companion object {
		fun mymain() {
			Log.addListener(System.out, LogLevel.INFO)
			try {
				Log.addOutputFile(File("logs/${StringUtil.currentTimestamp()}.txt"), LogLevel.DEBUG)
			} catch (e: FileNotFoundException) {
				Log.errorAndThrow(e)
			}
			Log.info("starting GL-Demo")
			var glDemo: GlDemo? = null
			JoglMain({ joglMain ->
				glDemo = GlDemo(joglMain)
			}, {
				if (glDemo != null)
					glDemo!!.mainLoop()
			})
		}
	}
	
	private fun mainLoop() {
		update()
		render()
	}
	
	val font = Font(24, "FreeSans")
	val keyboard: Keyboard
	val mouse: Mouse
	val camera = Camera()
	var elapsed: Double = 0.toDouble()
	val moveSpeed = 3.0
	val rotSpeed = MathUtil.toRadian(220.0)
	val timer = Timer(5.0)
	val fpsComputer = FrameRateComputer()
	var sphere: Model
	var light: PositionalLight = PositionalLight()
	val slider = Slider(0.5)
	val sliderb = Slider(0.5)
	val nurbs = Nurbs(5, 5)
	val simpleGrid = SimpleGrid(25, 25)
	val modelManager = ModelManager.instance
	val shaderManager = ShaderManager.instance
	val programManager = ProgramManager.instance
	val textureManager = TextureManager.instance
	var testProgram: Program = ProgramManager.DEFAULT
	var functionProgram: Program = ProgramManager.DEFAULT
	var colorProgram: Program = ProgramManager.DEFAULT
	var textureProgram: Program = ProgramManager.DEFAULT
	
	init {
		keyboard = joglMain.keyboard
		mouse = joglMain.mouse
		Log.info("init Demo, ${joglMain.canvas.width}, ${joglMain.canvas.height} canvas")
		sliderb.value = .3
		light = PositionalLight()
		light.ambient = Color.WHITE
		light.diffuse = Color.WHITE
		light.specular = Color.WHITE
		light.position = Vector3(-2.0, 4.0, 4.0)
		
		camera.upVector = Camera.DEFAULT_UP_VECTOR
		camera.position = Vector3(-1.0, 1.0, -1.0)
		camera.lookAt(Vector3.ZERO)
		
		initResources()
		sphere = modelManager["sphere.obj"]
		sphere.position = Vector3(0, 10, 0)
		
		reset()
		GlUtil.setClearColor(Color.BLACK)
	}
	
	private fun initResources() {
		modelManager.load("sphere.obj")
		modelManager.load("cube.obj")
		modelManager.load("cylinder.off")
		modelManager.load("monkey.off")
		textureManager.load("ground")
		textureManager.load("redgrid")
	}
	
	private fun setupFrame() {
		camera.apply()
		light.shine()
		drawCoordinateSystem()
		drawLight()
	}
	
	private fun reset() {
		Log.info("reloading")
		Config.game.reload()
		joglMain.title = Config.game["windowTitle"]
		joglMain.setSize(Config.game["windowWidth"].toInt(), Config.game["windowHeight"].toInt())
		val grid = Grid(6, 6)
		grid.arrangePlane(Vector3(-1.0, 0.0, -1.0), Vector3(1.0, 0.0, -1.0), Vector3(-1.0, 0.0, 1.0))
		grid.randomizeAxis(Vector3(0.0, 1.0, 0.0), -2.0, 2.0)
		nurbs.create(grid)
		nurbs.scaling = Vector3(15.0, 2.0, 15.0)
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
		joglMain.printGlInfo(gl())
	}
	
	fun render() {
		testProgram.use()
		setupFrame()
		Material.MELLOW.use()
//		sphere.render()
//		drawTexturedQuad()
		Color(1.0, 0.0, 0.0, 1.0).let { Material(it, it, it).use() }
//		nurbs.render()
		drawFontRectangleAndHud()
		drawFunctionGrid()
	}
	
	private fun drawFunctionGrid() {
		Program.push()
		functionProgram.use()
		simpleGrid.render()
		Program.pop()
	}
	
	private fun drawFontRectangleAndHud() {
//		TrueTypeFont
		Program.push()
		textureProgram.use()
		Hud.renderAllCorners()
		val x = font.createTexturedRectangle("boobar 1233 yy brOWN FOX JUMPS", Color.CYAN)
		x.position = Vector3(-0.1, 0.0)
//		x.lookInDirection(Vector3(1.0, 1.0, 1.7))
		GlUtil.toggleProjectionMode()
		val oldViewMatrix = UniformManager.getViewMatrix()
		UniformManager.setViewMatrix(Matrix.identity())
		x.render(height = 0.5 * sliderb.value)
		UniformManager.setViewMatrix(oldViewMatrix)
		GlUtil.toggleProjectionMode()
		Program.pop()
	}
	
	private fun drawTexturedQuad() {
		val z = -40
		Program.push()
		textureProgram.use()
		textureManager["ground"].bind()
		gl().glBegin(GL.GL_QUADS)
		GlUtil.directTexCoord(Vector2(0, 0))
		GlUtil.direct(Vector3(-10, -10, z))
		GlUtil.directTexCoord(Vector2(1, 0))
		GlUtil.direct(Vector3(10, -10, z))
		GlUtil.directTexCoord(Vector2(1.0, 0.5))
		GlUtil.direct(Vector3(10, 10, z))
		GlUtil.directTexCoord(Vector2(0, 1))
		GlUtil.direct(Vector3(-10, 10, z))
		gl().glEnd()
		Program.pop()
	}
	
	
	fun update() {
		fpsComputer.update()
		elapsed = fpsComputer.elapsed
		Timer.updateAll(elapsed)
		if (timer.isExpired) {
			Log.debug("FPS: " + fpsComputer.averageFps)
			Hud.lowerLeft.addLine("foo bar ${sliderb.value}")
			Hud.lowerRight.addLine("${slider.value} ddd aa")
			Hud.upperLeft.addLine(RANDOM.nextInt().string)
			Hud.upperRight.addLine(RANDOM.nextInt().string)
			timer.reset()
		}
		processInput()
	}
	
	private fun processInput() {
		updateSlider()
		moveOrientableObject(camera, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_C, KeyEvent.VK_X)
		moveObject(light, KeyEvent.VK_D, KeyEvent.VK_A, KeyEvent.VK_E, KeyEvent.VK_Q, KeyEvent.VK_S, KeyEvent.VK_W)
		if (keyboard.wasPressed(KeyEvent.VK_T))
			GlUtil.toggleWireframeMode()
		if (keyboard.wasPressed(KeyEvent.VK_Y))
			GlUtil.toggleProjectionMode()
		if (keyboard.wasPressed(KeyEvent.VK_U))
			GlUtil.toggleShadeModel()
		if (keyboard.wasPressed(KeyEvent.VK_I))
			GlUtil.toggleEnabled(GL.GL_LIGHTING)
		if (keyboard.wasPressed(KeyEvent.VK_O))
			GlUtil.toggleEnabled(GL.GL_AUTO_NORMAL)
		if (keyboard.wasPressed(KeyEvent.VK_P))
			saveScreenshot()
		if (keyboard.wasPressed(KeyEvent.VK_R))
			reset()
		processMouse()
	}
	
	private fun processMouse() {
//		if (mouse.wasPressed(MouseEvent.BUTTON1)) {
//			val c = mouse.currentNormalizedScreenCoordinates
//			val dir = camera.computeScreenRayDirection(c)
//			light.position = camera.position + 4 * dir.normalize()
//		}
	}
	
	private fun moveOrientableObject(`object`: MovableOrientable,
	                                 xp: Int, xn: Int, yp: Int,
	                                 yn: Int, zp: Int, zn: Int) {
		if (keyboard.isDown(zp))
			`object`.advance(moveSpeed * elapsed)
		if (keyboard.isDown(zn))
			`object`.advance(-moveSpeed * elapsed)
		if (keyboard.isDown(yp))
			`object`.yawAroundOtherUpVector(-rotSpeed * elapsed, Vector3.Y)
		if (keyboard.isDown(yn))
			`object`.yawAroundOtherUpVector(rotSpeed * elapsed, Vector3.Y)
		if (keyboard.isDown(xp))
			`object`.pitch(rotSpeed * elapsed)
		if (keyboard.isDown(xn))
			`object`.pitch(-rotSpeed * elapsed)
	}
	
	private fun moveObject(movable: Movable, xp: Int, xn: Int, yp: Int, yn: Int, zp: Int, zn: Int) {
		var delta = Vector3.ZERO
		val keys = intArrayOf(xp, xn, yp, yn, zp, zn)
		val dirs = arrayOf(Vector3.X, Vector3.NX, Vector3.Y, Vector3.NY, Vector3.Z, Vector3.NZ)
		for (i in keys.indices) {
			if (keyboard.isDown(keys[i])) {
				delta = delta.plus(dirs[i])
			}
		}
		movable.move(delta.times(elapsed * moveSpeed))
	}
	
	private fun drawLight() {
		colorProgram.use()
		light.material.use()
		val lightBall = modelManager["sphere.obj"]
		lightBall.scale(0.33)
		lightBall.position = light.position
		lightBall.render()
		testProgram.use()
	}
	
	/*
	fun processInput(mouse: Mouse) {
		aimingDirection = null
		val button1 = mouse.getButton(BUTTON1)
		
		val viewProjection = UniformManager.getViewProjectionMatrix()
		val projectedPlayerPosition = MathUtil.getScreenCoordinates(Vector3(), viewProjection,
				GlUtil.aspect)
		if (projectedPlayerPosition != null) {
			if (button1.wasReleased()) {
				shootFromTo(projectedPlayerPosition, mouse.currentNormalizedScreenCoordinates)
			} else if (button1.isDown) {
				val diff = mouse.currentNormalizedScreenCoordinates.minus(projectedPlayerPosition)
				aimingDirection = diff.normalize().times(5.0).plus(Vector2())
			}
		}
	}
	 */
	
	private fun drawCoordinateSystem() {
		colorProgram.use()
		val axisLength = 20.0
		Material.RED.use()
		drawLine(Vector3.NX * axisLength, Vector3.X * axisLength)
		Material.GREEN.use()
		drawLine(Vector3.NY * axisLength, Vector3.Y * axisLength)
		Material.BLUE.use()
		drawLine(Vector3.NZ * axisLength, Vector3.Z * axisLength)
	}
	
	private fun drawLine(a: Vector3, b: Vector3) {
		gl().glBegin(GL.GL_LINES)
		GlUtil.direct(a)
		GlUtil.direct(b)
		gl().glEnd()
	}
	
	private fun updateSlider() {
		if (keyboard.isDown(KeyEvent.VK_F))
			slider.progress(-elapsed)
		if (keyboard.isDown(KeyEvent.VK_G))
			slider.progress(elapsed)
		if (keyboard.isDown(KeyEvent.VK_V))
			sliderb.progress(-elapsed)
		if (keyboard.isDown(KeyEvent.VK_B))
			sliderb.progress(elapsed)
	}
	
	private fun saveScreenshot() {
		val file = File("screenshots", StringUtil.currentTimestamp() + ".jpg")
		GlUtil.saveScreenshot(file)
		Log.info("saved screenshot " + file)
	}
}
