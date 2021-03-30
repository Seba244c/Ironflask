package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.math.Vector2f;

public class GUIDynamicVector {
	public GUIDynamicVar x;
	public GUIDynamicVar y;
	
	public GUIDynamicVector() {
		
	}
	
	public GUIDynamicVector(GUIDynamicVar x, GUIDynamicVar y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f calculate(Window window) {
		return new Vector2f(x.calculate(window, false), y.calculate(window, true));
	}
}
