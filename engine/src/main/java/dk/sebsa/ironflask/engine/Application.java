package dk.sebsa.ironflask.engine;

import static org.lwjgl.glfw.GLFW.*;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.LayerStack;
import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.io.Window;
import dk.sebsa.ironflask.engine.math.Color;

public class Application {
	public String name;
	public LayerStack stack;
	public Window window;
	public Input input;
	
	public Application(String name) {
		this.name = name;
		
		// Window, stack and input
		stack = new LayerStack(this, name + "-LayerStack");
		window = new Window(name, 800, 500, true, Color.red(), this);
		input = new Input(this);
		input.addCallbacks();
	}
	
	public void run() {
		while(!window.shouldClose()) {
			// Window stuff
			glfwPollEvents();
			stack.handleEvents();
			window.update();
			input.update();
			
			// Logic
			Event event = new Event(EventType.AppUpdate, EventCatagory.App);
			event.oneLayer = false;
            event.dispatch(stack);
            
            // Render
            stack.render();

            // Endoff
            input.late();
			glfwSwapBuffers(window.windowId);
		}

		input.cleanup();
		window.cleanup();
		stack.cleanup();
	}
}
