package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.gui.enums.Anchor;
import dk.sebsa.ironflask.engine.math.Vector2f;

public abstract class GuiObject {
	public Rect rect;
	public Material material = new Material();
	private Anchor anchor;
	public GUIDynamicVector posistion;
	public GUIDynamicVector size;
	
	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
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
	
	public abstract void render(Shader shader, Mesh2d mesh, Rect r);
	public abstract boolean handleEvent(Event e);
}
