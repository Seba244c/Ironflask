package dk.sebsa.ironflask.engine.gui.animations;

import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.gui.Animation;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Parent;
import dk.sebsa.ironflask.engine.gui.enums.Side;
import dk.sebsa.ironflask.engine.math.Time;

public class MoveInFromSide extends Animation {
	private Side side;
	private float moveDistance;
	private Parent parent;
	
	public MoveInFromSide(Side side, Parent parent, float time, float waitTime) {
		super(time, waitTime);
		this.side = side;
		this.parent = parent;
	}

	@Override
	public void prepareRect(GuiObject obj, Rect input) {
		if(side == Side.Left) {
			moveDistance = input.x + input.width;
			input.x = 0 - input.width;
		} else if(side == Side.Right) {
			moveDistance = -1 * parent.getRect().width;
			input.x += parent.getRect().width;
		} else if(side == Side.Top) {
			moveDistance = input.y + input.height;
			input.y = 0 - input.height;
		} else if(side == Side.Bottom) {
			moveDistance = -1 * parent.getRect().height;
			input.y += parent.getRect().height;
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
