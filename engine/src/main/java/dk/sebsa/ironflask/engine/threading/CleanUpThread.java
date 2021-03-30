package dk.sebsa.ironflask.engine.threading;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.AssetManager;
import dk.sebsa.ironflask.engine.core.LayerStack;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;

public class CleanUpThread extends Thread {
	public int state = 0;
	private GLCapabilities capabilities;
	private Application app;
	
	public CleanUpThread(LayerStack stack, Application app) {
		this.app = app;
		this.capabilities = app.window.capabilities;
	}
	
	@Override
	public void run() {
		LoggingUtil.coreLog(Severity.Info, "Starting CleanUpThread");
		glfwMakeContextCurrent(app.window.windowId);
		GL.setCapabilities(capabilities);
				
		// Cleans
		AssetManager.cleanup();
		app.audioManager.cleanup();
		app.input.cleanup();
		app.stack.cleanup();
		app.fbo.cleanup();
		
		// Done
		state = 1;
		LoggingUtil.coreLog(Severity.Info, "Cleanup state: 1");
		LoggingUtil.saveToFile();
		app.window.cleanup();
		state = 2;
		LoggingUtil.coreLog(Severity.Info, "Closing CleanUpThread");
	}
}
