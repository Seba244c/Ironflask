package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;

public class GUIDynamicVar {
	public GUIDynamicType type;
	public float value;
	
	public GUIDynamicVar (GUIDynamicType type, float value) {
		this.type = type;
		this.value = value;
	}
	
	protected float calculate(Window window, boolean y) {
		if(type == GUIDynamicType.Fixed) return value;
		
		if(y) {
			return value * window.rect.height;
		}
		return value * window.rect.width;
	}
}

