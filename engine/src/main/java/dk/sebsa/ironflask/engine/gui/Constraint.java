package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;

public class Constraint {
	public ConstraintSide constraintSide;
	public GUIDynamicVar var;
	
	public Constraint(ConstraintSide constraintSide, GUIDynamicVar var) {
		this.constraintSide = constraintSide;
		this.var = var;
	}
}