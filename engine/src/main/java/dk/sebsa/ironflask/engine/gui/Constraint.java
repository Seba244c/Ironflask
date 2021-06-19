package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.gui.enums.Side;

public class Constraint {
	public Side constraintSide;
	public GUIDynamicVar var;
	
	public Constraint(Side constraintSide, GUIDynamicVar var) {
		this.constraintSide = constraintSide;
		this.var = var;
	}
}
