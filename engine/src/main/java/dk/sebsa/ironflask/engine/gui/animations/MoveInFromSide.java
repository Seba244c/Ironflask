package dk.sebsa.ironflask.engine.gui.animations;

import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.gui.Animation;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.math.Time;

public class MoveInFromSide extends Animation {
	public enum Side {
		Top,
		Bottom,
		Right,
		Left
	}
	
	private Side side;
	private float moveDistance;
	private Window window;
	
	public MoveInFromSide(Side side, Window window, float time, float waitTime) {
		super(time, waitTime);
		this.side = side;
		this.window = window;
	}

	@Override
	public void prepareRect(GuiObject obj, Rect input) {
		if(side == Side.Left) {
			moveDistance = input.x + input.width;
			input.x = 0 - input.width;
		} else if(side == Side.Right) {
			moveDistance = -1 * window.rect.width;
			input.x += window.rect.width;
		} else if(side == Side.Top) {
			moveDistance = input.y + input.height;
			input.y = 0 - input.height;
		} else if(side == Side.Bottom) {
			moveDistance = -1 * window.rect.height;
			input.y += window.rect.height;
		}
	}

	@Override
	public void update(GuiObject obj, Rect nowR) {
		if(side == Side.Left || side == Side.Right) {
			nowR.x += Time.getDeltaTime() * moveDistance/time;
		} else {
			nowR.y += Time.getDeltaTime() * moveDistance/time;
		}
		
		if(now >= time+waitTime) {
			nowR.set(endGoal.x, endGoal.y, endGoal.width, endGoal.height);
		}
	}
}
