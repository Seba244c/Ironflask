package dk.sebsa.ender.game.layers;

import org.joml.Vector3f;

import dk.sebsa.ender.game.components.OrbitCamera;
import dk.sebsa.ender.game.components.PlayerMovement;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.ecs.CameraEntity;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.components.EntityRenderer;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer3d;

public class EnderGame extends Layer {
	private Application application;
	private Renderer3d renderer;
	private CameraEntity camera;
	private Entity player;
	
	public EnderGame(Application app) {
		super();
		this.application = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			renderer.windowResized();
			application.skyboxRenderer.windowResized();
		} else if(e.type == EventType.AppLate) {
			camera.lateUpdate();
		}
		
		return Component.assingedInput.onEvent(e);
	}
	
	@Override
	public void close() {
		// ee
	}
	
	@Override
	public void render() {
		renderer.render(camera);
	}

	@Override
	public void init() {
		// Set app Shader and renderer
		try {
			renderer = new Renderer3d(application);
		} catch (Exception e) { e.printStackTrace(); }
		// Random block
		Entity randomBlock = new Entity(true);
		randomBlock.addComponent(new EntityRenderer(Mesh.getMesh("cube.obj"), new Material(), Shader.getShader("default")));
		randomBlock.setLocalPosition(new Vector3f(0, -1, 0));
		randomBlock = new Entity(true);
		randomBlock.addComponent(new EntityRenderer(Mesh.getMesh("cube.obj"), new Material(), Shader.getShader("default")));
		randomBlock.setLocalPosition(new Vector3f(2, -1, 1));
		
		// Player
		player = new Entity(true);
		player.addComponent(new EntityRenderer(Mesh.getMesh("cube.obj"), new Material(Texture.getTexture("grassblock.png")), Shader.getShader("default")));
		player.addComponent(new PlayerMovement());
		// Camera
		camera = new CameraEntity(false);
		camera.parent(player);
		camera.setLocalPosition(new Vector3f(0, 0, 4));
		camera.addComponent(new OrbitCamera(player));
		
		// end off
		Entity.recalculate();
	}
}
