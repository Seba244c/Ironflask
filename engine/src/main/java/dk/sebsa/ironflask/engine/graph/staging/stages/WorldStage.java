package dk.sebsa.ironflask.engine.graph.staging.stages;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.graph.FBO;
import dk.sebsa.ironflask.engine.graph.staging.RenderingStage;

public class WorldStage extends RenderingStage {
	public WorldStage(Application app) {
		super(app);
	}

	@Override
	public void draw(FBO prevFBO) {
		renderPrevFBO(prevFBO);
		WorldManager.onWillRenderAll();
        app.stack.render();
	}

	@Override
	public void windowChangedSize() {
		// NOTHINGNESS
	}
}
