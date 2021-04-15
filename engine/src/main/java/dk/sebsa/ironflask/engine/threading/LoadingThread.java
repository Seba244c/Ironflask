package dk.sebsa.ironflask.engine.threading;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

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
import dk.sebsa.ironflask.engine.enums.ThreadState;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer2d;
import dk.sebsa.ironflask.engine.graph.renderers.GuiRenderer;
import dk.sebsa.ironflask.engine.graph.renderers.SkyboxRenderer;
import dk.sebsa.ironflask.engine.graph.staging.stages.GUIStage;
import dk.sebsa.ironflask.engine.graph.staging.stages.WorldStage;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.math.Time;

public class LoadingThread extends Thread {
	public ThreadState state = ThreadState.Doing;
	private GLCapabilities capabilities;
	private Application app;
	private LayerStack stack;
	
	public LoadingThread(LayerStack stack, Application app) {
		this.stack = stack;
		this.app = app;
		this.capabilities = app.window.capabilities;
	}
	
	public void renderLoadScreen(boolean createAssets, float load) {
		LoggingUtil.coreLog(Severity.Info, "Rendering Splash Screen");
		if(createAssets) {
			// Create shader and texture
			try {
				Renderer2d.init(app.window, new Shader("/ironflask_2d"));
				new Texture("/Splash.png");
				new Texture("/Pixel.png");
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		//// RENDER
		// Window
		app.window.update();
		// Splash icon
		Renderer2d.prepare();
		
		Renderer2d.drawTextureWithTextCoords(Texture.getTexture("Splash.png"), new Rect(0, 0, app.window.getWidth(), app.window.getHeight()), new Rect(0, 0, 1,1));
		if(load != -1f) {
			float cutOff = app.window.getWidth() * 0.2f;
			float cutOffPrSide = cutOff/2;
			
			// White
			float y = app.window.getHeight()*0.8f;
			Renderer2d.defaultShader.setUniformAlt("color", Color.darkGrey());
			Renderer2d.drawTextureWithTextCoords(Texture.getTexture("Pixel.png"), new Rect(cutOffPrSide, y, app.window.getWidth()*0.8f, 35), new Rect(0, 0, 1,1));
			
			// Red
			Renderer2d.defaultShader.setUniformAlt("color", Color.dimGrey());
			Renderer2d.drawTextureWithTextCoords(Texture.getTexture("Pixel.png"), new Rect(cutOffPrSide+4, y+5, (app.window.getWidth()*0.8f-8)*load, 35-10), new Rect(0, 0, 1,1));
		}
		
		Renderer2d.unprepare();
		glfwSwapBuffers(app.window.windowId);
	}

	@Override
	public void run() {
		LoggingUtil.coreLog(Severity.Info, "Starting LoadingThread");
		GLFW.glfwMakeContextCurrent(app.window.windowId);
		GL.setCapabilities(capabilities);
		
		renderLoadScreen(true, 0);
		
		// Input
		app.input = new Input(app);
		renderLoadScreen(false, 0.1f);
		app.input.addCallbacks();
		renderLoadScreen(false, 0.15f);
		Component.assingedInput = new ComponentInput(app.input);
		
		// Audio
		renderLoadScreen(false, 0.2f);
		app.audioManager = new AudioManager();
		renderLoadScreen(false, 0.35f);
		app.audioManager.setInstance();
		
		// Assets
		renderLoadScreen(false, 0.4f);
		try {
			AssetManager.loadAllResources(Paths.get(".").toAbsolutePath().normalize().toString() + "/resources/");
		} catch (Exception e) { state = ThreadState.Failed; e.printStackTrace(); return; }
		
		// Renderers
		renderLoadScreen(false, 0.7f);
		app.skyboxRenderer = new SkyboxRenderer(app);
		renderLoadScreen(false, 0.78f);
		app.guiRenderer = new GuiRenderer(app);
		
		// Other
		renderLoadScreen(false, 0.9f);
		app.pipeline.add(new WorldStage(app));
		renderLoadScreen(false, 0.94f);
		app.pipeline.add(new GUIStage(app));
		stack.init();
		Time.init();
		
		renderLoadScreen(false, 1);
		
		// Done
		GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
		state = ThreadState.Done;
		LoggingUtil.coreLog(Severity.Info, "Closing LoadingThread");
	}
}
