package dk.sebsa.ironflask.engine;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
import dk.sebsa.ironflask.engine.graph.staging.RenderingStage;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer2d;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.io.Window;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.math.Time;
import dk.sebsa.ironflask.engine.threading.CleanUpThread;
import dk.sebsa.ironflask.engine.threading.LoadingThread;
import dk.sebsa.ironflask.engine.utils.BuildUtil;

public class Application {
	public String name;
	public LayerStack stack;
	public Window window;
	public AudioManager audioManager;
	public Input input;
	public final boolean isDebug;
	public AppState state = AppState.Loading;
	private byte logic = 1;
	private Consumer<Application> loadingFinishedCallback;
	public Consumer<Application> loadCallback;
	
	private Event updateEvent;
	
	public LoadingThread loadingThread;

	public SkyboxRenderer skyboxRenderer;
	public GuiRenderer guiRenderer;
	public List<RenderingStage> pipeline = new ArrayList<>();
	
	public Application(String name, boolean isDebug, Consumer<Application> loadingFinishedCallback, int width, int height) {
		this.name = name;
		this.isDebug = isDebug;
		this.loadingFinishedCallback = loadingFinishedCallback;
		
		// Print stream
		System.setOut(new LoggingUtil.RedirectPrintStream(System.out));
		System.setErr(new LoggingUtil.RedirectErrPrintStream(System.err));
		
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
	        System.err.println("Uncaught exception in thread " + thread.getName());
	        throwable.printStackTrace(System.err);
	    });
		
		BuildUtil.init();
		LoggingUtil.coreLog(Severity.Info, "Buildversion: " + BuildUtil.id);
		
		// Window, stack and input
		stack = new LayerStack(this, name + "-LayerStack");
		window = new Window(name, width, height, true, Color.cyan(), this);
		window.setClearColor(Color.cyan());
		glfwMakeContextCurrent(MemoryUtil.NULL);
		loadingThread = new LoadingThread(stack, this);
		loadingThread.start();
		
		updateEvent = new Event(EventType.AppUpdate, EventCatagory.App);
		updateEvent.oneLayer = false;
	}
	
	public Application(String name, boolean isDebug, Consumer<Application> loadingFinishedCallback) {
		this(name, isDebug, loadingFinishedCallback, 800, 500);
	}
	
	public Application(String name, boolean isDebug, Consumer<Application> loadingFinishedCallback, Consumer<Application> loadCallback) {
		this(name, isDebug, loadingFinishedCallback);
		this.loadCallback = loadCallback;
	}
	
	public void pauseLogic(boolean pause) {
		logic = (byte) (pause ? 0 : 1);
	}
	
	public boolean isPaused() {
		return logic == 0;
	}
	
	public void run() {
		try {
			while(!window.shouldClose()) {
				if(state == AppState.Loading) {
					loadingState();
					if(loadingThread.state != ThreadState.Doing || !loadingThread.isAlive()) {
						if(loadingThread.state == ThreadState.Failed) {
							window.setShouldClose(true);
							LoggingUtil.coreLog(Severity.Info, "Loading thread failed. Closing Gracefully");
						}
						state = AppState.Running;
						
						LoggingUtil.coreLog(Severity.Info, "Stealing back GLFW and AL capabalities");
						glfwMakeContextCurrent(window.windowId);
						GL.setCapabilities(window.capabilities);
						AL.createCapabilities(audioManager.deviceCaps);
				        alcMakeContextCurrent(audioManager.context);
				        
				        if(loadingFinishedCallback != null) loadingFinishedCallback.accept(this);
				        Event event = new Event(EventType.AppFinishedLoading, EventCatagory.App);
			    		event.oneLayer = false;
			            event.dispatch(stack);
					}
				} else runningState();
			}
		} catch (Exception e) {
			LoggingUtil.coreLog(Severity.Info, "Run function caused exception. Closing Gracefully");
			e.printStackTrace();
			
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
		window.cleanup();
	}
	
	public void setClearColor(Color color) {
		window.setClearColor(color);
		for(RenderingStage stage : pipeline) {
			stage.setClearColor(color);
		}
	}
	
	public void cleanupScreen() {
		try {
			loadingThread.renderLoadScreen(false, -1f);
		} catch (Exception e) {
			
		}
	}
	
	public void loadingState() {
		// Window stuff
		glfwPollEvents();
	}
	
	private Rect r = new Rect(0, 1, 1, -1);
	public void render() {
		FBO fbo = null;
		for(RenderingStage stage : pipeline) {
			fbo = stage.render(fbo);
		}

		Renderer2d.prepare();
		Renderer2d.drawTextureWithTextCoords(fbo.getTexture(), window.getRect(), r);
		Renderer2d.unprepare();
	}
	
	public void windowResized() {
		for(RenderingStage stage : pipeline) {
			stage.updateFbo(false, window.getClearColor());
		}
	}
	
	public void addRenderingStage(RenderingStage stage) {
		pipeline.add(pipeline.size()-1, stage);
	}
	
	public void runningState() {
		// Window stuff
		glfwPollEvents();
		stack.handleEvents();
		window.update();
		input.update();
		Time.process();
		
		// Logic
		if(logic == 1) {
			WorldManager.getAllEntities();
			WorldManager.updateAll();
			
			updateEvent.dispatch(stack);
	        WorldManager.lateUpdateAll();
		}
        
        // Render
        render();
        
        // Endoff
    	input.late();
        if(logic == 1) {
            Event event = new Event(EventType.AppLate, EventCatagory.App);
    		event.oneLayer = false;
            event.dispatch(stack);
        }
		glfwSwapBuffers(window.windowId);
	}
	
	public void close() {
		window.setShouldClose(true);
	}
}
