package dk.sebsa.ironflask.engine.ecs;

import java.util.ArrayList;
import java.util.List;

public class WorldManager {
	private static World world = new World();
	private static List<Entity> entities = new ArrayList<>();
	private static int i;
	
	public static void updateAll() {
		// Occurs on every frame before rendering
		for(int i = 0; i < entities.size(); i++) {
			if(!entities.get(i).isEnabled()) continue;
			for(Component c : entities.get(i).getComponents()) {
				if(!c.isEnabled()) continue;
				c.update();
			}
		}
	}
	
	public static void lateUpdateAll() {
		// Occurs after update
		for(int i = 0; i < entities.size(); i++) {
			if(!entities.get(i).isEnabled()) continue;
			for(Component c : entities.get(i).getComponents()) {
				if(!c.isEnabled()) continue;
				c.lateUpdate();
			}
		}
	}
	
	public static void onWillRenderAll() {
		// When ready to render
		for(int i = 0; i < entities.size(); i++) {
			if(!entities.get(i).isEnabled()) continue;
			for(Component c : entities.get(i).getComponents()) {
				if(!c.isEnabled()) continue;
				c.onWillRender();
			}
		}
	}
	
	public static void getAllEntities() {
		entities.clear();
		List<Entity> updateList = new ArrayList<>();
		updateList.add(Entity.master);
		while(updateList.size() > 0) {
			Entity entity = updateList.get(0);
			List<Entity> children = entity.getChildren();
			if(children.size() > 0) {
				for(i = 0; i < children.size(); i++) {
					updateList.add(0, children.get(i));
				}
			}
			
			if(entity == Entity.master) {
				updateList.remove(updateList.size() - 1);
				continue;
			}
			entities.add(entity);
			updateList.remove(entity);
		}
	}

	public static World getWorld() {
		return world;
	}

	public static void setWorld(World world) {
		WorldManager.world = world;
	}
}
