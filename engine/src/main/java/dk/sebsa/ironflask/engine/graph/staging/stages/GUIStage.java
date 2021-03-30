package dk.sebsa.ironflask.engine.graph.staging.stages;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.graph.staging.RenderingStage;

public class GUIStage extends RenderingStage {
	public GUIStage(Application app) {
		super(app);
	}

	@Override
	public void draw() {
        app.stack.renderGUI();
	}
}
