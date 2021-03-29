package dk.sebsa.ender.game.layers;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.ecs.CameraEntity;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.ecs.Entity;
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
		
		// Camera
		camera = new CameraEntity(true);
		//camera.addComponent(new CameraMovement());
		
		// Player
		player = new Entity(true);
		
		// end off
		Entity.recalculate();
	}
}
