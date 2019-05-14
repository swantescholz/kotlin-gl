package gl

import com.sun.opengl.util.Animator
import com.sun.opengl.util.FPSAnimator
import gl.gfx.GlUtil
import gl.input.Keyboard
import gl.input.Mouse
import util.log.Log
import util.stacktrace
import java.awt.GraphicsEnvironment
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.media.opengl.GL
import javax.media.opengl.GLAutoDrawable
import javax.media.opengl.GLCanvas
import javax.media.opengl.GLEventListener
import javax.swing.JFrame

// create instance, init your resources, then call start()
class JoglMain(private val mainGameSetUp: (JoglMain) -> Unit,
               private val mainGameLoop: () -> Unit) : JFrame(), GLEventListener {
	
	val keyboard = Keyboard()
	val mouse = Mouse()
	
	var animator: FPSAnimator
	var canvas: GLCanvas
	
	var isFullscreen = false
		set(fullscreen) {
			if (fullscreen == this.isFullscreen)
				return
			
			val visible = isVisible
			val gd = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
			
			if (fullscreen) {
				field = true
				isVisible = false
				dispose()
				isUndecorated = true
				isResizable = false
				gd.fullScreenWindow = this
			} else {
				field = false
				gd.fullScreenWindow = null
				isVisible = false
				dispose()
				isUndecorated = false
				isResizable = true
				setSize(600, 400)
			}
			
			isVisible = visible
		}
	private var listenersInitialized = false
	
	
	init {
		canvas = GLCanvas()
		GlUtil.initGl(canvas.gl!!)
		canvas.addGLEventListener(this)
		
		canvas.addKeyListener(object : KeyAdapter() {
			override fun keyPressed(e: KeyEvent?) {
				if (e!!.keyCode == KeyEvent.VK_ESCAPE) {
					killWindow()
				}
			}
		})
		
		animator = FPSAnimator(canvas, 60)
		title = "One Day at MY-Camp"
		setSize(800, 600)
		
		val pane = this.contentPane
		pane.add(canvas)
		
		setLocationRelativeTo(null)
		
		addWindowListener(object : WindowAdapter() {
			override fun windowClosing(e: WindowEvent?) {
				killWindow()
			}
		})
		isVisible = true
	}
	
	private fun killWindow() {
		Log.info("Exit...")
		isVisible = false
		this.dispose()
		System.exit(0)
	}
	
	override fun setVisible(newVisibleState: Boolean) {
		val wasVisible = isVisible
		super.setVisible(newVisibleState)
		if (wasVisible != newVisibleState) {
			if (newVisibleState) {
				getAnimator().start()
				canvas.requestFocus()
			} else {
				getAnimator().stop()
			}
		}
	}
	
	override fun init(drawable: GLAutoDrawable) {
		if (!listenersInitialized) {
			listenersInitialized = true
			drawable.addMouseListener(mouse)
			drawable.addMouseMotionListener(mouse)
			drawable.addMouseWheelListener(mouse)
			drawable.addKeyListener(keyboard)
		}
		GlUtil.initGl(drawable.gl)
		printGlInfo(drawable.gl)
		try {
			mainGameSetUp(this)
		} catch (e: Exception) {
			Log.error("mainGameSetUp failed with exception:")
			Log.error(e.stacktrace)
			Log.error("closing window")
			killWindow()
		}
	}
	
	fun printGlInfo(gl: GL) {
		Log.debug("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR))
		Log.debug("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER))
		Log.debug("GL_VERSION: " + gl.glGetString(GL.GL_VERSION))
	}
	
	override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
		val gl = drawable.gl
		GlUtil.reshapeGlView(gl, x, y, width, height)
	}
	
	override fun displayChanged(drawable: GLAutoDrawable, modeChanged: Boolean, deviceChanged: Boolean) {
	}
	
	override fun display(drawable: GLAutoDrawable) {
		val gl = drawable.gl
		GlUtil.initFrame(gl)
		try {
			mainGameLoop()
		} catch (e: Exception) {
			Log.warn("mainGameLoop failed with exception:")
			Log.warn(e)
			Log.warn("closing window")
			killWindow()
		}
	}
	
	fun getAnimator(): Animator {
		return animator
	}
	
}