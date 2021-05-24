package dk.sebsa.ironflask.editor.ui.modules;

import dk.sebsa.ironflask.editor.ui.EditorLayer;
import dk.sebsa.ironflask.editor.ui.Module;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.WorldChangedEvent;
import dk.sebsa.ironflask.engine.ecs.World;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.objects.Box;
import dk.sebsa.ironflask.engine.gui.objects.GuiList;

public class WorldView extends Module {
	private World world;
	private GuiList list;
	
	public WorldView(String title, Sprite window, EditorLayer editor) {
		super(title, window, editor);
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.CurrentWorldChanged) {
			world = ((WorldChangedEvent) e).newCurrentWorld;
			populate();
		}
		
		return super.handleEvent(e);
	}
	
	public void populate() {
		list = new GuiList(guiWindow);
		
		new Box(list);
	}
	
	@Override
	public void init() {
		if(world == null) world = WorldManager.getWorld();
		
		populate();
	}
}
