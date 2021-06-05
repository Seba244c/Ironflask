package dk.sebsa.ironflask.editor.ui.modules;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.editor.ui.EditorLayer;
import dk.sebsa.ironflask.editor.ui.Module;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.WorldChangedEvent;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.World;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVar;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVector;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.gui.objects.GuiList;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;

public class WorldView extends Module {
	private World world;
	private GuiList list;
	private Font font;
	
	public WorldView(String title, Sprite window, EditorLayer editor, Application app) {
		super(title, window, editor, app);
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.CurrentWorldChanged) {
			world = ((WorldChangedEvent) e).newCurrentWorld;
			populate();	
			calculateConstraints();
		}
		
		return super.handleEvent(e);
	}
	
	private List<Entity> updateList = new ArrayList<>();
	public void populate() {
		list = new GuiList(guiWindow);
		font = Font.getFont(new java.awt.Font("OpenSans", 0, 18));
		GUIDynamicVector size = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 1f), new GUIDynamicVar(GUIDynamicType.Fixed, 20));
		
		updateList.add(WorldManager.getWorld().master);
		while(updateList.size() > 0) {
			Entity entity = updateList.get(0);
			List<Entity> children = entity.getChildren();
			
			if(children.size() > 0) {
				for(int i = 0; i < children.size(); i++) {
					updateList.add(0, children.get(i));
				}
			}
			
			if(entity == WorldManager.getWorld().master) {
				updateList.remove(updateList.size() - 1);
				continue;
			}
			
			Button e = new Button(list, null, new Label(entity.getName(), font), false);
			e.size = size;
			
			updateList.remove(entity);
		}
	}
	
	@Override
	public void init() {
		if(world == null) world = WorldManager.getWorld();
		
		populate();	
		calculateConstraints();
	}
}
