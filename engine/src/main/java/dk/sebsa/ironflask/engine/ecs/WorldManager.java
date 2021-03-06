package dk.sebsa.ironflask.engine.ecs;

import java.util.ArrayList;
import java.util.List;

public class WorldManager {
	public static List<Entity> entities = new ArrayList<>();
	
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
}
