package dk.sebsa.ironflask.sandbox.layers;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.audio.AudioClip;
import dk.sebsa.ironflask.engine.audio.AudioSource;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.ecs.CameraEntity;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.ecs.components.AudioListener;
import dk.sebsa.ironflask.engine.ecs.components.EntityRenderer;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.SkyBox;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer3d;
import dk.sebsa.ironflask.sandbox.Main;
import dk.sebsa.ironflask.sandbox.components.CameraMovement;
import dk.sebsa.ironflask.sandbox.components.Spin;

public class GameLayer extends Layer {
	private Application application;
	private Renderer3d renderer;
	private CameraEntity camera;
	private Entity blockMaster;
	private AudioSource musicSource;
	
	public GameLayer(Application app) {
		super();
		this.application = app;
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
			} else if(e2.key == GLFW.GLFW_KEY_ESCAPE) {
				application.close();
				return true;
			} else if(e2.key == GLFW.GLFW_KEY_P) {
				if(musicSource.isPlaying()) musicSource.pause();
				else musicSource.play();
				return true;
			} else if(e2.key == GLFW.GLFW_KEY_E) {
				blockMaster.setLocalPosition(new Vector3f(0, 100, 0));
				return true;
			}
		} else if(e.type == EventType.WindowResize) {
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
		application.skyboxRenderer.render(camera);
		renderer.render(camera);
	}

	@Override
	public void init() {
		// Set app Shader and renderer
		try {
			renderer = new Renderer3d(application);
		} catch (Exception e) { e.printStackTrace(); }
		blockMaster = new Entity("Blockmaster");
		blockMaster.addComponent(new Spin());
		
		// Player
		camera = new CameraEntity(true);
		camera.addComponent(new CameraMovement());
		camera.addComponent(new AudioListener(application.audioManager));
		// Music
		musicSource = new AudioSource(true, false);
		musicSource.setClip(AudioClip.getClip("RememberTheHeroes"));
		// Cubes
		try {
			Mesh mesh = Mesh.getMesh("cube.obj");
			Shader shader = Shader.getShader("default");
			Material material = new Material(Texture.getTexture("grassblock.png"));
			for(int i = 0; i < 10; i++) {
				for(int e = 0; e < 10; e++) {
					Entity entity = new Entity(false);
					entity.parent(blockMaster);
					entity.setLocalScale(0.5f);
					entity.setLocalPosition(new Vector3f(i*1f, 0, e*1f));
					EntityRenderer er = new EntityRenderer(mesh, material, shader);
					entity.addComponent(er);
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		Entity.recalculate();
		
		// Skybox
		WorldManager.getWorld().skyBox = new SkyBox(new Material(Texture.getTexture("default_skybox.png")));
	}
}
