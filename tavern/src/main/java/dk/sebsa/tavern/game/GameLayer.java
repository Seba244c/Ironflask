package dk.sebsa.tavern.game;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.ecs.CameraEntity;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.components.EntityRenderer;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer3d;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.tavern.game.components.CameraControl;

public class GameLayer extends Layer {
	private CameraEntity camera;
	private Renderer3d renderer;
	private Application application;
	
	public GameLayer(Application app) {
		this.application = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			renderer.windowResized();
		} else if(e.type == EventType.AppLate) {
			camera.lateUpdate();
		} else if(e.type == EventType.KeyPressed) {
			KeyPressedEvent event = (KeyPressedEvent) e;
			if(event.key == GLFW.GLFW_KEY_TAB) {
				if(Main.debug.isEnabled()) Main.debug.setEnabled(false);
				else Main.debug.setEnabled(true);
				return true;
			}
		}
				
		return Component.assingedInput.onEvent(e);
	}

	@Override
	public void render() {
		camera.moveRotation(1.2f, 1.7f, 0.3f);
		renderer.render(camera);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// Set app Shader and renderer
		try {
			renderer = new Renderer3d(application);
		} catch (Exception e) { e.printStackTrace(); }
		
		// Create camera
		camera = new CameraEntity(true);
		camera.addComponent(new CameraControl());
		camera.setLocalPosition(new Vector3f(0, 0, 4));
		camera.doNotDelete();
		
		// Create flat
		Entity flat = new Entity("Flat");
		flat.setLocalPosition(new Vector3f(4, 0, 4));
		flat.addComponent(new EntityRenderer(Mesh.getMesh("flat.obj"), new Material(Color.green()), Shader.getShader("default")));
		flat.setLocalScale(4f);
		
		Entity cube = new Entity("Flat");
		cube.setLocalPosition(new Vector3f(4, 0, 4));
		cube.addComponent(new EntityRenderer(Mesh.getMesh("cube.obj"), new Material(Color.green()), Shader.getShader("default")));
		
		// OOps
		Entity.recalculate();
		renderer.windowResized();
	}
}
