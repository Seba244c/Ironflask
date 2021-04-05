package dk.sebsa.ironflask.engine.gui.animations;

import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.gui.Animation;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.math.Time;

public class ScaleUp extends Animation {
	@Override
	public void prepareRect(GuiObject obj, Rect input) {
		obj.scale = 0f;
	}

	@Override
	public void update(GuiObject obj, Rect nowR) {
		now += Time.getDeltaTime();
		if(now < waitTime) return;
		System.out.println(obj.scale);
		obj.scale += Time.getDeltaTime() / time;
		
		if(now >= time+waitTime) {
			obj.scale = 1f;
			stop();
		}
	}
}
