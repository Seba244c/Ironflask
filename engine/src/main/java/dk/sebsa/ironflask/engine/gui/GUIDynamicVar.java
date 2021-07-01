package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.math.Mathf;

public class GUIDynamicVar {
	public GUIDynamicType type;
	public float value;
	
	public GUIDynamicVar (GUIDynamicType type, float value) {
		this.type = type;
		this.value = value;
	}
	
	protected float calculate(Parent parent, boolean y) {
		if(type == GUIDynamicType.Fixed) return value;
		
		value = Mathf.clamp(value, 0, 1);
		if(y) {
			return value * parent.getRect().height;
		}
		return value * parent.getRect().width;
	}
	
	public GUIDynamicVar clone() {
		return new GUIDynamicVar(type, value);
	}
	
	@Override
	public String toString() {
		return type + ", "+value;
	}
}

