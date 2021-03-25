package dk.sebsa.ironflask.engine;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.system.MemoryUtil;

import dk.sebsa.ironflask.engine.core.AssetManager;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.LayerStack;
import dk.sebsa.ironflask.engine.core.LoadingThread;
import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.enums.AppState;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.io.Window;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.math.Time;

public class Application {
	public String name;
	public LayerStack stack;
	public Window window;
	public Input input;
	public final boolean isDebug;
	public AppState state = AppState.Loading;
	
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
	
	public void run() {
		while(!window.shouldClose()) {
			if(state == AppState.Loading) {
				loadingState();
				if(loadingThread.done) {
					state = AppState.Running;
					glfwMakeContextCurrent(window.windowId);
				}
			} else runningState();
		}
		input.cleanup();
		window.cleanup();
		stack.cleanup();
		AssetManager.cleanup();
	}
	
	public void loadingState() {
		// Window stuff
		glfwPollEvents();
	}
	
	public void runningState() {
		// Window stuff
		glfwPollEvents();
		stack.handleEvents();
		window.update();
		input.update();
		Time.process();
		
		// Logic
		WorldManager.updateAll();
		Event event = new Event(EventType.AppUpdate, EventCatagory.App);
		event.oneLayer = false;
        event.dispatch(stack);
        WorldManager.lateUpdateAll();
        
        // Render
        WorldManager.onWillRenderAll();
        stack.render();

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
