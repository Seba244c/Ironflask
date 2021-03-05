package dk.sebsa.ironflask.engine;

import static org.lwjgl.glfw.GLFW.*;

import dk.sebsa.ironflask.engine.core.events.Event;
import dk.sebsa.ironflask.engine.core.events.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.events.Event.EventType;
import dk.sebsa.ironflask.engine.core.layer.LayerStack;
import dk.sebsa.ironflask.engine.io.Window;
import dk.sebsa.ironflask.engine.math.Color;

public class Application {
	public String name;
	public LayerStack stack;
	public Window window;
	
	public Application(String name) {
		this.name = name;
		
		stack = new LayerStack(this, name + "-LayerStack");
		window = new Window(name, 800, 500, true, Color.red(), this);
	}
	
	public void run() {

		while(!window.shouldClose()) {
			// Window stuff
			glfwPollEvents();
			stack.handleEvents();
			window.update();
			
			// Logic
			Event event = new Event(EventType.AppUpdate, EventCatagory.App);
			event.oneLayer = false;
            event.dispatch(stack);
            
            // Render
            stack.render();
			
			// Endoff
			glfwSwapBuffers(window.windowId);
		}
		
		window.cleanup();
		stack.cleanup();
	}
}
