package dk.sebsa.ironflask.engine.graph.staging.stages;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.graph.FBO;
import dk.sebsa.ironflask.engine.graph.staging.RenderingStage;
import static org.lwjgl.opengl.GL11.*;

public class WorldStage extends RenderingStage {
	public WorldStage(Application app) {
		super(app);
	}

	@Override
	public void render(FBO prevFBO) {
		fbo.bindFrameBuffer();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		WorldManager.onWillRenderAll();
        app.stack.render();
		fbo.unBind();
	}
}
