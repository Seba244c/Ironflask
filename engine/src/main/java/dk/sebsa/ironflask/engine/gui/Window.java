package dk.sebsa.ironflask.engine.gui;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.math.Color;

public class Window {
	protected List<GuiObject> guiObjects = new ArrayList<>();
	protected List<Constraint> constraints = new ArrayList<>();
	private Color backgroundColor = Color.black();
	public Rect rect;
	public Rect textRect;
	public Rect renderRect;
	public Label label;
	private static Font font;
	public float fontHeight;
	
	public Window(String title) {
		if(font==null) {
			font = Font.getFont(new java.awt.Font("OpenSans", java.awt.Font.PLAIN, 24));
		}
		
		label = new Label(title, font);
		fontHeight = font.getFontHeight()+2;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
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
	
	public boolean handleEvent(Event e) {
		for(GuiObject object : guiObjects) {
			if(object.handleEvent(e)) return true;
		}
		return false;
	}
	
	public void calculateConstraints(Application app) {
		rect = new Rect(0, 0, app.window.getWidth(), app.window.getHeight());
		for(Constraint constraint : constraints) {
			if(constraint.var.type == GUIDynamicType.Dynamic) {
				if(constraint.constraintSide == ConstraintSide.Top) {
					float removePixels = constraint.var.value*app.window.getHeight();
					rect.y += removePixels;
				} else if(constraint.constraintSide == ConstraintSide.Bottom) {
					float removePixels = constraint.var.value*app.window.getHeight();
					rect.height -= removePixels;
				} else if(constraint.constraintSide == ConstraintSide.Right) {
					float removePixels = constraint.var.value*app.window.getWidth();
					rect.width -= removePixels;
				} else if(constraint.constraintSide == ConstraintSide.Left) {
					float removePixels = constraint.var.value*app.window.getWidth();
					rect.x += removePixels;
				}
			} else if(constraint.var.type == GUIDynamicType.Fixed) {
				if(constraint.constraintSide == ConstraintSide.Top) {
					rect.y += constraint.var.value;
				} else if(constraint.constraintSide == ConstraintSide.Bottom) {
					rect.height -= constraint.var.value;
				} else if(constraint.constraintSide == ConstraintSide.Right) {
					rect.width -= constraint.var.value;
				} else if(constraint.constraintSide == ConstraintSide.Left) {
					rect.x += constraint.var.value;
				}
			}
		}
		
		renderRect = rect.copy();
		textRect = rect.copy();
		rect.y += 31;
		rect.height -= fontHeight;
		rect.width -= 4;
		rect.x += 4;
		
		textRect.height -= rect.height-fontHeight;
		textRect.x += 4;
		textRect.width -= 4;
		
		for(GuiObject object : guiObjects) {
			object.calculateAnchors(this);
		}
		
		for(GuiObject object : guiObjects) object.prepare();
	}
}
