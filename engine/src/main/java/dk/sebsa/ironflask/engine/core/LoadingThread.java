package dk.sebsa.ironflask.engine.core;

import java.io.IOException;
import java.nio.file.Paths;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import dk.sebsa.ironflask.engine.io.Window;
import dk.sebsa.ironflask.engine.math.Time;

public class LoadingThread extends Thread {
	public boolean done = false;
	private GLCapabilities capabilities;
	private Window window;
	private LayerStack stack;
	
	public LoadingThread(LayerStack stack, Window window) {
		this.stack = stack;
		this.window = window;
		this.capabilities = window.capabilities;
	}

	@Override
	public void run() {
		GLFW.glfwMakeContextCurrent(window.windowId);
		GL.setCapabilities(capabilities);
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
