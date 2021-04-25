package dk.sebsa.ironflask.engine.graph.staging.stages;

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
	public void draw(FBO prevFBO) {
		Renderer2d.prepare();
		if(direction == FlipDirection.Vertical) {
			Renderer2d.drawTextureWithTextCoords(prevFBO.getTexture(), app.window.getRect(), new Rect(0, 0, 1, 1));
		} else {
			Renderer2d.drawTextureWithTextCoords(prevFBO.getTexture(), app.window.getRect(), new Rect(1, 1, -1, -1));
		}
		Renderer2d.unprepare();
	}

	@Override
	public void windowChangedSize() {
		
	}
}
