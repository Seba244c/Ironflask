package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.math.Time;

public abstract class Animation {
	public float time = 1.0f;
	public float now = 0;
	public float waitTime = 0.0f;
	public Rect endGoal;
	private boolean ready;
	
	public Animation(float time, float waitTime) {
		this.waitTime = waitTime;
		this.time = time;
	}
	
	public void prepare(GuiObject obj, Rect input) {
		this.endGoal = input.copy();
		ready = true;
		now = 0;
		prepareRect(obj, input);
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public abstract void prepareRect(GuiObject obj, Rect input);
	
	public void tick(GuiObject obj, Rect nowR) {
		now += Time.getDeltaTime();
		if(now < waitTime) return;
		
		update(obj, nowR);
		
		if(now >= time+waitTime) {
			ready = false;
		}
	}
	public abstract void update(GuiObject obj, Rect now);
}
