package dk.sebsa.ironflask.engine.threading;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import java.io.IOException;
import java.nio.file.Paths;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.audio.AudioManager;
import dk.sebsa.ironflask.engine.core.AssetManager;
import dk.sebsa.ironflask.engine.core.LayerStack;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.ecs.ComponentInput;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer2d;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
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
	
	public void renderLoadScreen(boolean createAssets) {
		LoggingUtil.coreLog(Severity.Info, "Rendering Splash Screen");
		if(createAssets) {
			// Create shader and texture
			try {
				Renderer2d.init(app.window, new Shader("/2d"));
				new Texture("/Splash.png");
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		//// RENDER
		// Window
		app.window.update();
		// Splash icon
		Renderer2d.prepare();
		Renderer2d.drawTextureWithTextCoords(Texture.getTexture("Splash.png"), new Rect(0, 0, app.window.getWidth(), app.window.getHeight()), new Rect(0, 0, 1,1));
		Renderer2d.unprepare();
		glfwSwapBuffers(app.window.windowId);
	}

	@Override
	public void run() {
		LoggingUtil.coreLog(Severity.Info, "Starting LoadingThread");
		GLFW.glfwMakeContextCurrent(app.window.windowId);
		GL.setCapabilities(capabilities);
		
		renderLoadScreen(true);
		
		// Input
		app.input = new Input(app);
		app.input.addCallbacks();
		Component.assingedInput = new ComponentInput(app.input);
		
		// Audio
		app.audioManager = new AudioManager();
		app.audioManager.setInstance();
		
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
		LoggingUtil.coreLog(Severity.Info, "Closing LoadingThread");
	}
}
