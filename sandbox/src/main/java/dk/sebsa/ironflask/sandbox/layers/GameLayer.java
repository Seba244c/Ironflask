package dk.sebsa.ironflask.sandbox.layers;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.ecs.CameraEntity;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.ecs.components.EntityRenderer;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Renderer3d;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.utils.FileUtil;
import dk.sebsa.ironflask.sandbox.Main;
import dk.sebsa.ironflask.sandbox.components.CameraMovement;
import dk.sebsa.ironflask.sandbox.components.Spin;

public class GameLayer extends Layer {
	private Application application;
	private Renderer3d renderer;
	private CameraEntity camera;
	
	public GameLayer(Application app) {
		super();
		this.application = app;
		
		// Set app Shader and renderer
		try {
			renderer = new Renderer3d(application);
		} catch (Exception e) { e.printStackTrace(); }
		
		// Entity
		camera = new CameraEntity();
		camera.addComponent(new CameraMovement());
		WorldManager.entities.add(camera);
		try {
			Mesh mesh = FileUtil.loadMesh("/models/cube.obj");;
			Texture texture = new Texture("/textures/grassblock.png");
			Shader shader = new Shader("default", application.isDebug);
			for(int i = 0; i < 10; i++) {
				Entity entity = new Entity();
				entity.setScale(0.5f);
				entity.setPosition(new Vector3f(i*0.6f, 0, -2));
				EntityRenderer er = new EntityRenderer(mesh, texture, shader);
				entity.addComponent(er);
				entity.addComponent(new Spin());
				WorldManager.entities.add(entity);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.KeyPressed) {
			KeyPressedEvent e2 = (KeyPressedEvent) e;
			if(e2.key == GLFW.GLFW_KEY_TAB) {
				if(Main.debug.enabled) Main.debug.enabled = false;
				else Main.debug.enabled = true;
				return true;
			} else if(e2.key == GLFW.GLFW_KEY_F7) {
				application.window.setLineMode(!application.window.getLineMode());
				return true;
			}
		} else if(e.type == EventType.WindowResize) {
			renderer.windowResized();
		}
		return false;
	}
	
	@Override
	public void close() {
		renderer.cleanup();
	}
	
	@Override
	public void render() {
		renderer.render(camera);
	}
}
