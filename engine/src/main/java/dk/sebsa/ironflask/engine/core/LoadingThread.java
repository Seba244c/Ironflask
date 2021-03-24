package dk.sebsa.ironflask.engine.core;

import java.io.IOException;
import java.nio.file.Paths;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.ecs.ComponentInput;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.math.Time;

public class LoadingThread extends Thread {
	public boolean done = false;
	private GLCapabilities capabilities;
	private Application app;
	private LayerStack stack;
	
	public LoadingThread(LayerStack stack, Application app) {
		this.stack = stack;
		this.app = app;
		this.capabilities = app.window.capabilities;
	}

	@Override
	public void run() {
		GLFW.glfwMakeContextCurrent(app.window.windowId);
		GL.setCapabilities(capabilities);
		
		// Input
		app.input = new Input(app);
		app.input.addCallbacks();
		Component.assingedInput = new ComponentInput(app.input);
		
		// Assets
		try {
			AssetManager.loadAllResources(Paths.get(".").toAbsolutePath().normalize().toString() + "/resources/");
		} catch (IOException e) { e.printStackTrace(); }
		
		// Other
		stack.init();
		Time.init();
		
		// Done
		GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
		done = true;
	}
}
