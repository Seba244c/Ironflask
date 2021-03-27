package dk.sebsa.ironflask.engine.ecs;

public class WorldManager {
	private static World world = new World();
	
	public static void updateAll() {
		// Occurs on every frame before rendering
		for(int i = 0; i < world.entities.size(); i++) {
			if(!world.entities.get(i).isEnabled()) continue;
			for(Component c : world.entities.get(i).getComponents()) {
				if(!c.isEnabled()) continue;
				c.update();
			}
		}
	}
	
	public static void lateUpdateAll() {
		// Occurs after update
		for(int i = 0; i < world.entities.size(); i++) {
			if(!world.entities.get(i).isEnabled()) continue;
			for(Component c : world.entities.get(i).getComponents()) {
				if(!c.isEnabled()) continue;
				c.lateUpdate();
			}
		}
	}
	
	public static void onWillRenderAll() {
		// When ready to render
		for(int i = 0; i < world.entities.size(); i++) {
			if(!world.entities.get(i).isEnabled()) continue;
			for(Component c : world.entities.get(i).getComponents()) {
				if(!c.isEnabled()) continue;
				c.onWillRender();
			}
		}
	}

	public static World getWorld() {
		return world;
	}

	public static void setWorld(World world) {
		WorldManager.world = world;
	}
}
