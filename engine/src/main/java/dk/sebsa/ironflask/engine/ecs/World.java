package dk.sebsa.ironflask.engine.ecs;

import dk.sebsa.ironflask.engine.graph.SkyBox;

public class World {
	public SkyBox skyBox;
	public Entity master = new Entity(false);
}
