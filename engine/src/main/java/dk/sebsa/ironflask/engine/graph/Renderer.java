package dk.sebsa.ironflask.engine.graph;

import dk.sebsa.ironflask.engine.ecs.CameraEntity;

public abstract class Renderer {
	public abstract void render(CameraEntity camera);
	public abstract void cleanup();
}
