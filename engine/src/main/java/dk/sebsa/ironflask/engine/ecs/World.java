package dk.sebsa.ironflask.engine.ecs;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.graph.SkyBox;

public class World {
	public List<Entity> entities = new ArrayList<>();
	public SkyBox skyBox;
}
