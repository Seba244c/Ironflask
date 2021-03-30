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
		float xf = x.calculate(window, false);
		float yf = y.calculate(window, true);
		if(yf % 1 != 0) yf += 0.5f;
		if(xf % 1 != 0) xf += 0.5f;
		return new Vector2f(xf, yf);
	}
}
