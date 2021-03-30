package dk.sebsa.ironflask.engine.gui;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintType;
import dk.sebsa.ironflask.engine.math.Color;

public class Window {
	private List<GuiObject> guiObjects = new ArrayList<>();
	private List<Constraint> constraints = new ArrayList<>();
	private Color backgroundColor = Color.black();
	public Rect rect;
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void addCosntraint(Constraint constraint) {
		constraints.add(constraint);
	}
	
	public void addGuiObject(GuiObject object) {
		guiObjects.add(object);
	}
	
	public List<GuiObject> getGuiObjects() {
		return guiObjects;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public void calculateConstraints(Application app) {
		rect = new Rect(0, 0, app.window.getWidth(), app.window.getHeight());
		for(Constraint constraint : constraints) {
			if(constraint.constraintType == ConstraintType.Dynamic) {
				if(constraint.constraintSide == ConstraintSide.Top) {
					float removePixels = constraint.value*app.window.getHeight();
					rect.y += removePixels;
				} else if(constraint.constraintSide == ConstraintSide.Bottom) {
					float removePixels = constraint.value*app.window.getHeight();
					rect.height -= removePixels;
				} else if(constraint.constraintSide == ConstraintSide.Right) {
					float removePixels = constraint.value*app.window.getWidth();
					rect.width -= removePixels;
				} else if(constraint.constraintSide == ConstraintSide.Left) {
					float removePixels = constraint.value*app.window.getWidth();
					rect.x += removePixels;
				}
			} else if(constraint.constraintType == ConstraintType.Fixed) {
				if(constraint.constraintSide == ConstraintSide.Top) {
					rect.y += constraint.value;
				} else if(constraint.constraintSide == ConstraintSide.Bottom) {
					rect.height -= constraint.value;
				} else if(constraint.constraintSide == ConstraintSide.Right) {
					rect.width -= constraint.value;
				} else if(constraint.constraintSide == ConstraintSide.Left) {
					rect.x += constraint.value;
				}
			}
		}
	}
}
