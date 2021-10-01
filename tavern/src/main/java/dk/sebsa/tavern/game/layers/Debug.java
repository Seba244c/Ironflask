package dk.sebsa.tavern.game.layers;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.GUILayer;
import dk.sebsa.ironflask.engine.debug.ImGuiLayer;

@GUILayer
public class Debug extends ImGuiLayer {	
	public Debug(Application application) {
		super(application);
	}
	
	@Override
	public void drawCustom() {
		
	}
}
