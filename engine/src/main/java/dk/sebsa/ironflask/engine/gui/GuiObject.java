package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.gui.enums.Anchor;
import dk.sebsa.ironflask.engine.gui.enums.GuiObjects;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class GuiObject {
	public GuiObjects objectType;
	public Rect rect;
	private Color backgroundColor = Color.black();
	private Anchor anchor;
	public GUIDynamicVector posistion;
	public GUIDynamicVector size;
	
	public GuiObject(GuiObjects objectType) {
		this.objectType = objectType;
	}
	
	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public void calculateAnchors(Window window) {
		Vector2f sizeVector2f = size.calculate(window);
		Vector2f pos = posistion.calculate(window);
		rect = new Rect(0, 0, sizeVector2f.x, sizeVector2f.y);
		
		if(anchor == Anchor.BottomLeft) {
			rect.y = window.rect.height;
			rect.x += pos.x;
			rect.y -= pos.y;
		} else if(anchor == Anchor.BottomMiddle) {
			rect.x = window.rect.width/2;
			rect.y = window.rect.height;
			rect.x += pos.x;
			rect.y -= pos.y;
		} else if(anchor == Anchor.BottomRight) {
			rect.x = window.rect.width;
			rect.y = window.rect.height;
			rect.x -= pos.x;
			rect.y -= pos.y;
		} else if(anchor == Anchor.LeftMiddle) {
			rect.y = window.rect.height / 2;
			rect.x += pos.x;
			rect.y += pos.y;
		} else if(anchor == Anchor.RightMiddle) {
			rect.y = window.rect.height / 2;
			rect.x = window.rect.width;
			rect.x -= pos.x;
			rect.y += pos.y;
		} else if(anchor == Anchor.TopLeft) {
			rect.x += pos.x;
			rect.y += pos.y;
		} else if(anchor == Anchor.TopRight) {
			rect.x = window.rect.width;
			rect.x -= pos.x;
			rect.y += pos.y;
		} else if(anchor == Anchor.TopMiddle) {
			rect.x = window.rect.width/2;
			rect.x += pos.x;
			rect.y += pos.y;
		}
	}
	
	public void draw() {
		
	}
}
