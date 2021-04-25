package dk.sebsa.ironflask.engine.graph.staging.stages;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.graph.FBO;
import dk.sebsa.ironflask.engine.graph.staging.RenderingStage;

public class GUIStage extends RenderingStage {
	public GUIStage(Application app) {
		super(app);
	}

	@Override
	public void draw(FBO prevFBO) {
		renderPrevFBO(prevFBO);
        app.stack.renderGUI();
	}

	@Override
	public void windowChangedSize() {
		// NOTHINGNESS
	}
}
