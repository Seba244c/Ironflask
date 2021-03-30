package dk.sebsa.ironflask.engine.graph.staging.stages;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.enums.FlipDirection;
import dk.sebsa.ironflask.engine.graph.FBO;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer2d;
import dk.sebsa.ironflask.engine.graph.staging.RenderingStage;

public class FlipStage extends RenderingStage {
	private FlipDirection direction;
	
	public FlipStage(Application app, FlipDirection direction) {
		super(app);
		this.direction = direction;
	}
	
	@Override
	public FBO render(FBO prevFBO) {
		if(!enabled) return prevFBO;
		
		fbo.bindFrameBuffer();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer2d.prepare();
		if(direction == FlipDirection.Vertical) {
			Renderer2d.drawTextureWithTextCoords(prevFBO.getTexture(), app.window.getRect(), new Rect(0, 0, 1, 1));
		} else {
			Renderer2d.drawTextureWithTextCoords(prevFBO.getTexture(), app.window.getRect(), new Rect(1, 1, -1, -1));
		}
		Renderer2d.unprepare();
		draw();
		fbo.unBind();
		
		return fbo;
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowChangedSize() {
		
	}
}
