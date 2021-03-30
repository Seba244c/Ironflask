package dk.sebsa.ironflask.engine;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import dk.sebsa.ironflask.engine.audio.AudioManager;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.LayerStack;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.enums.AppState;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.enums.ThreadState;
import dk.sebsa.ironflask.engine.graph.FBO;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.renderers.GuiRenderer;
import dk.sebsa.ironflask.engine.graph.renderers.SkyboxRenderer;
import dk.sebsa.ironflask.engine.graph.renderers.SplashScreenRenderer;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.io.Window;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.math.Time;
import dk.sebsa.ironflask.engine.threading.CleanUpThread;
import dk.sebsa.ironflask.engine.threading.LoadingThread;

public class Application {
	public String name;
	public LayerStack stack;
	public Window window;
	public AudioManager audioManager;
	public Input input;
	public final boolean isDebug;
	public AppState state = AppState.Loading;
	public SkyboxRenderer skyboxRenderer;
	public GuiRenderer guiRenderer;
	public FBO fbo;
	
	public LoadingThread loadingThread;
	
	public Application(String name, boolean isDebug) {
		this.name = name;
		this.isDebug = isDebug;
		
		// Window, stack and input
		stack = new LayerStack(this, name + "-LayerStack");
		window = new Window(name, 800, 500, true, Color.cyan(), this);
		
		glfwMakeContextCurrent(MemoryUtil.NULL);
		loadingThread = new LoadingThread(stack, this);
		loadingThread.start();
	}
	
	public void updateFbo() {
		fbo = new FBO(window.getWidth(), window.getHeight());
		fbo.bindFrameBuffer();
		// Enable depth test
		glEnable(GL_DEPTH_TEST);
		glClearColor(0, 1, 1, 1);
		fbo.unBind();
	}
	
	public void run() {
		while(!window.shouldClose()) {
			if(state == AppState.Loading) {
				loadingState();
				if(loadingThread.state != ThreadState.Doing) {
					if(loadingThread.state == ThreadState.Failed) {
						window.setShouldClose(true);
						LoggingUtil.coreLog(Severity.Info, "Loading thread failed. Closing Gracefully");
					}
					state = AppState.Running;
					
					LoggingUtil.coreLog(Severity.Info, "Stealing back GLFW and AL capabalities");
					glfwMakeContextCurrent(window.windowId);
					AL.createCapabilities(audioManager.deviceCaps);
			        alcMakeContextCurrent(audioManager.context);
				}
			} else runningState();
		}
		LoggingUtil.coreLog(Severity.Info, "##--## Beginning cleanup procsess ##--##");
		cleanupScreen();
		
		// clean up thread
		glfwMakeContextCurrent(MemoryUtil.NULL);
		GL.setCapabilities(null);
		
		CleanUpThread thread = new CleanUpThread(stack, this);
		thread.start();
		
		while(thread.state == 0) {
			glfwPollEvents();
		}
		
		while(thread.state == 1) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
	
	public void cleanupScreen() {
		loadingThread.renderLoadScreen(false);
	}
	
	public void loadingState() {
		// Window stuff
		glfwPollEvents();
	}
	
	public void render() {
		fbo.bindFrameBuffer();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		WorldManager.onWillRenderAll();
        stack.render();
		fbo.unBind();

		SplashScreenRenderer.prepare();
		SplashScreenRenderer.drawTextureWithTextCoords(fbo.getTexture(), window.getRect(), new Rect(0, 1, 1, -1));
		SplashScreenRenderer.unprepare();
	}
	
	public void runningState() {
		// Window stuff
		glfwPollEvents();
		stack.handleEvents();
		window.update();
		input.update();
		Time.process();
		
		// Logic
		WorldManager.getAllEntities();
		WorldManager.updateAll();
		Event event = new Event(EventType.AppUpdate, EventCatagory.App);
		event.oneLayer = false;
        event.dispatch(stack);
        WorldManager.lateUpdateAll();
        
        // Render
        render();
        
        // Endoff
        input.late();
        event = new Event(EventType.AppLate, EventCatagory.App);
		event.oneLayer = false;
        event.dispatch(stack);
		glfwSwapBuffers(window.windowId);
	}
	
	public void close() {
		window.setShouldClose(true);
	}
}
