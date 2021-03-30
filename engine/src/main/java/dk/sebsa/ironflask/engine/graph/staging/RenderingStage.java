package dk.sebsa.ironflask.engine.graph.staging;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.graph.FBO;
import dk.sebsa.ironflask.engine.io.Window;

public abstract class RenderingStage {
	public FBO fbo;
	public Application app;
	private Window window;
	
	public RenderingStage(Application app) {
		this.app = app;
		this.window = app.window;
		updateFbo();
	}
	
	public void updateFbo() {
		if(fbo != null) fbo.cleanup();
		fbo = new FBO(window.getWidth(), window.getHeight());
		fbo.bindFrameBuffer();
		// Enable depth test
		glEnable(GL_DEPTH_TEST);
		glClearColor(0, 1, 1, 1);
		fbo.unBind();
	}
	
	public void cleanup() {
		fbo.cleanup();
	}
	
	public abstract void render(FBO prevFBO);
}
