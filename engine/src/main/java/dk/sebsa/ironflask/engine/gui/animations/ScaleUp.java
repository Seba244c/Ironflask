package dk.sebsa.ironflask.engine.gui.animations;

import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.gui.Animation;
import dk.sebsa.ironflask.engine.gui.GuiObject;

public class ScaleUp extends Animation {
	public ScaleUp(float time, float waitTime) {
		super(time, waitTime);
	}

	@Override
	public void prepareRect(GuiObject obj, Rect input) {
		obj.scale = 0f;
	}

	@Override
	public void update(GuiObject obj, Rect nowR) {
		obj.scale = now / time;
		
		if(now >= time+waitTime) {
			obj.scale = 1f;
		}
	}
}
