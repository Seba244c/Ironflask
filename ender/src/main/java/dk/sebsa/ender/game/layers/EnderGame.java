package dk.sebsa.ender.game.layers;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import dk.sebsa.ender.game.Main;
import dk.sebsa.ender.game.components.OrbitCamera;
import dk.sebsa.ender.game.components.PlayerMovement;
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
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer3d;
import dk.sebsa.ironflask.engine.math.Color;

public class EnderGame extends Layer {
	private Application application;
	private Renderer3d renderer;
	private CameraEntity camera;
	private Entity player;
	private OrbitCamera cameraComponent;
	
	public static float configSensitevity = 1;
	
	public EnderGame(Application app) {
		super();
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
		
		if(cameraComponent.onEvent(e)) return true;
		
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
		Material redMaterial = new Material();
		Material yellowMaterial = new Material();
		yellowMaterial.setColor(Color.yellow());
		Entity randomBlock = new Entity(Main.testWorld);
		randomBlock.addComponent(new EntityRenderer(Mesh.getMesh("cube.obj"), redMaterial, Shader.getShader("default")));
		randomBlock.setLocalPosition(new Vector3f(0, -1, 0));
		randomBlock = new Entity(Main.testWorld);
		randomBlock.addComponent(new EntityRenderer(Mesh.getMesh("cube.obj"), redMaterial, Shader.getShader("default")));
		randomBlock.setLocalPosition(new Vector3f(2, -1, 1));
		randomBlock = new Entity(Main.testWorld);
		randomBlock.addComponent(new EntityRenderer(Mesh.getMesh("cube.obj"), yellowMaterial, Shader.getShader("default")));
		randomBlock.setLocalPosition(new Vector3f(80, -1, 1));
		randomBlock.setLocalScale(5);
		
		// Player
		PlayerMovement pm = new PlayerMovement();
		player = new Entity(Main.testWorld);
		player.addComponent(new EntityRenderer(Mesh.getMesh("cube.obj"), new Material(Texture.getTexture("grassblock.png")), Shader.getShader("default")));
		player.addComponent(pm);
		player.doNotDelete();
		// Camera
		OrbitCamera oc = new OrbitCamera(player);
		camera = new CameraEntity(false);
		camera.parent(player);
		camera.setLocalPosition(new Vector3f(0, 0, 4));
		cameraComponent = oc;
		camera.addComponent(cameraComponent);
		player.doNotDelete();
		pm.setCameraComponent(oc);
		// end off
		Entity.recalculate();
	}
}
