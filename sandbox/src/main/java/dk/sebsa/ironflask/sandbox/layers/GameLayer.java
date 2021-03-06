package dk.sebsa.ironflask.sandbox.layers;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Renderer3d;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.sandbox.Main;

public class GameLayer extends Layer {
	private Application application;
	private Renderer3d renderer;
	
	float[] positions = new float[]{
	        -0.5f,  0.5f, 0.0f,
	        -0.5f, -0.5f, 0.0f,
	         0.5f, -0.5f, 0.0f,
	         0.5f,  0.5f, 0.0f,
	    };
	int[] indices = new int[]{
	        0, 1, 3, 3, 1, 2,
	};
	float[] colours = new float[]{
		    0.5f, 0.0f, 0.0f,
		    0.0f, 0.5f, 0.0f,
		    0.0f, 0.0f, 0.5f,
		    0.0f, 0.5f, 0.5f,
	};
	private Mesh mesh;
	
	public GameLayer(Application app) {
		super();
		this.application = app;
		
		// Set app Shader and renderer
		try {
			renderer = new Renderer3d(new Shader("default"));
		} catch (Exception e) { e.printStackTrace(); }
		
		// Mesh
		mesh = new Mesh(positions, colours, indices);
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.KeyPressed) {
			KeyPressedEvent e2 = (KeyPressedEvent) e;
			if(e2.key == GLFW.GLFW_KEY_TAB) {
				if(Main.debug.enabled) Main.debug.enabled = false;
				else Main.debug.enabled = true;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void close() {
		renderer.cleanup();
	}
	
	@Override
	public void render() {
		renderer.render(mesh);
	}
}
