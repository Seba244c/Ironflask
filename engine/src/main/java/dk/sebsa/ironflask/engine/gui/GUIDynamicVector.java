package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class GUIDynamicVector {
	public GUIDynamicVar x;
	public GUIDynamicVar y;
	private Vector2f v = new Vector2f();
	
	public GUIDynamicVector() {
		
	}
	
	public GUIDynamicVector(GUIDynamicVar x, GUIDynamicVar y) {
		this.x = x;
		this.y = y;
		
		if(x == null) {
			this.x = new GUIDynamicVar(GUIDynamicType.Dynamic, 0);
		}
		if(y == null) {
			this.y = new GUIDynamicVar(GUIDynamicType.Dynamic, 0);
		}
	}
	
	public Vector2f calculate(Parent parent) {
		float xf, yf;
		xf = x.calculate(parent, false);
		
		yf = y.calculate(parent, true);

		if(yf % 1 != 0) yf += 0.5f;
		if(xf % 1 != 0) xf += 0.5f;
		return v.set(xf, yf);
	}
	
	public GUIDynamicVector clone() {
		return new GUIDynamicVector(x.clone(), y.clone());
	}
}
