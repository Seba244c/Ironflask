package dk.sebsa.ironflask.engine.graph.staging;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.graph.FBO;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer2d;
import dk.sebsa.ironflask.engine.io.Window;

public abstract class RenderingStage {
	public FBO fbo;
	public Application app;
	public boolean enabled = true;
	private Window window;
	
	public RenderingStage(Application app) {
		this.app = app;
		this.window = app.window;
		
		updateFbo(true);
	}
	
	public void updateFbo(boolean init) {
		if(fbo != null) fbo.cleanup();
		fbo = new FBO(window.getWidth(), window.getHeight());
		fbo.bindFrameBuffer();
		// Enable depth test
		glEnable(GL_DEPTH_TEST);
		glClearColor(0, 1, 1, 1);
		fbo.unBind();
		if(!init) windowChangedSize();
	}
	
	public void cleanup() {
		fbo.cleanup();
	}
	
	public FBO render(FBO prevFBO) {
		if(!enabled) return prevFBO;
		
		fbo.bindFrameBuffer();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		renderPrevFBO(prevFBO);
		draw();
		fbo.unBind();
		
		return fbo;
	}
	
	private void renderPrevFBO(FBO prevFBO) {
		if(prevFBO == null) return;
		Renderer2d.prepare();
		Renderer2d.drawTextureWithTextCoords(prevFBO.getTexture(), window.getRect(), new Rect(0, 1, 1, -1));
		Renderer2d.unprepare();
	}
	
	public abstract void draw();
	public abstract void windowChangedSize();
}
