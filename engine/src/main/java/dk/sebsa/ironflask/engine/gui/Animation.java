package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.graph.Rect;

public abstract class Animation {
	public float time = 1.0f;
	public float now = 0;
	public float waitTime = 0.0f;
	public Rect endGoal;
	private boolean ready;
	
	public void prepare(Rect input) {
		this.endGoal = input.copy();
		ready = true;
		now = 0;
		prepareRect(input);
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void stop() {
		ready = false;
	}
	
	public abstract void prepareRect(Rect input);
	public abstract void update(Rect now);
}
