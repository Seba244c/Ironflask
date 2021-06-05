package dk.sebsa.ironflask.editor.ui;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.editor.ui.modules.*;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.enums.Languages;
import dk.sebsa.ironflask.engine.gui.Constraint;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVar;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.SpriteSheet;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.local.LocalizationManager;

public class EditorLayer extends Layer {
	private Application app;
	private Sprite window;
	private List<Module> modules = new ArrayList<>();
	public Entity selected;
	public Object inspected;
	
	public EditorLayer(Application app) {
		guiLayer = true;
		this.app = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			for(Module module : modules) {
				module.calculateConstraints(app);
			}
		}
		
		for(Module module : modules) {
			if(module.handleEvent(e)) return true;
		}
		return false;
	}

	@Override
	public void render() {
		app.guiRenderer.prepareForRender();
		
		for(Module module : modules) {
			module.render(app);
		}
		
		app.guiRenderer.endFrame();
	}

	@Override
	public void close() {
		
	}

	@Override
	public void init() {
		LocalizationManager.setLangauage(Languages.en);
		
		SpriteSheet sheet = SpriteSheet.getSheet("Ironflask_BlackGUI");
		window = sheet.getSprite("Window");
		
		// Create window layout
		Module inspector	 = new Inspector(LocalizationManager.getString("gui.inspectorWindow.title"), window, this, app);
		Module world		 = new WorldView(LocalizationManager.getString("gui.worldWindow.title"), window, this, app);
		Module asset		 = new Assets(LocalizationManager.getString("gui.assetWindow.title"), window, this, app);
		
		world.addCosntraint(new Constraint(ConstraintSide.Bottom, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.3f)));
		world.addCosntraint(new Constraint(ConstraintSide.Right, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.8f)));
		asset.addCosntraint(new Constraint(ConstraintSide.Top, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.7f)));
		asset.addCosntraint(new Constraint(ConstraintSide.Right, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.2f)));
		inspector.addCosntraint(new Constraint(ConstraintSide.Left, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.8f)));
		
		modules.add(inspector);
		modules.add(world);
		modules.add(asset);
		
		for(Module module : modules) {
			module.init();
		}
	}
}
