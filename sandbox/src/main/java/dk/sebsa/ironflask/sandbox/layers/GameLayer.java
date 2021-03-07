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
import dk.sebsa.ironflask.sandbox.Main;
import dk.sebsa.ironflask.sandbox.components.CameraMovement;
import dk.sebsa.ironflask.sandbox.components.Spin;

public class GameLayer extends Layer {
	private Application application;
	private Renderer3d renderer;
	private CameraEntity camera;
	
	float[] positions = new float[] {
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,
            
            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,

            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,

            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,

            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            
            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,

            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,

            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
        };
        int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            6,14,4,6,15,14,
            // Bottom face
            18,16,17,17,19,18,
            // Back face
            7,4,5,7,6,4,};
	
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
			Mesh mesh = new Mesh(positions, textCoords, indices);
			Texture texture = new Texture("/textures/grassblock.png");
			Shader shader = new Shader("default");
			for(int i = 0; i < 10; i++) {
				Entity entity = new Entity();
				entity.setScale(0.5f);
				entity.setPosition(new Vector3f(i*0.6f, 0, -2));
				EntityRenderer er;
				er = new EntityRenderer(mesh, texture, shader);
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
