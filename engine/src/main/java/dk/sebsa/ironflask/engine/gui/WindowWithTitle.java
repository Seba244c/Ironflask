package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;

public class WindowWithTitle extends Window {
	public Label label;
	private static Font font;
	public Rect textRect;
	
	public WindowWithTitle(String title) {
		if(font==null) {
			font = Font.getFont(new java.awt.Font("OpenSans", java.awt.Font.PLAIN, 24));
		}
		
		label = new Label(title, font);
	}
	
	@Override
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

		textRect = new Rect(rect.x+4, rect.y, rect.width, font.getFontHeight()+2);
		
		rect.y += font.getFontHeight()+2;
		rect.height -= font.getFontHeight()+2;
		
		for(GuiObject object : guiObjects) {
			object.calculateAnchors(this);
		}
		
		for(GuiObject object : guiObjects) object.prepare();
	}
}
