package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintType;

public class Constraint {
	public ConstraintSide constraintSide;
	public ConstraintType constraintType;
	public float value;
	
	public Constraint(ConstraintSide constraintSide, ConstraintType constraintType, float value) {
		this.constraintSide = constraintSide;
		this.constraintType = constraintType;
		this.value = value;
	}
}
