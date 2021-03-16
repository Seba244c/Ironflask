package dk.sebsa.ironflask.engine;

import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;
import java.nio.file.Paths;

import dk.sebsa.ironflask.engine.core.AssetManager;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.LayerStack;
import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.ecs.ComponentInput;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
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
	
	public Application(String name, boolean isDebug) {
		this.name = name;
		this.isDebug = isDebug;
		
		// Window, stack and input
		stack = new LayerStack(this, name + "-LayerStack");
		window = new Window(name, 800, 500, true, Color.red(), this);
		input = new Input(this);
		input.addCallbacks();
		Component.assingedInput = new ComponentInput(input);
		
		// Resources
		try {
			AssetManager.loadAllResources(Paths.get(".").toAbsolutePath().normalize().toString() + "/resources/");
		} catch (IOException e) { e.printStackTrace(); }
		
		// Time
		Time.init();
	}
	
	public void run() {
		while(!window.shouldClose()) {
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

		input.cleanup();
		window.cleanup();
		stack.cleanup();
		AssetManager.cleanup();
	}
}
