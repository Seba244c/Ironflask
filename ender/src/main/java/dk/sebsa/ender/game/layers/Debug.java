package dk.sebsa.ender.game.layers;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.layer.ImGuiLayer;

public class Debug extends ImGuiLayer {
	public Debug(Application app) {
		super(app);
		guiLayer = true;
	}

	@Override
	public void drawCustom() {
		
	}
}
