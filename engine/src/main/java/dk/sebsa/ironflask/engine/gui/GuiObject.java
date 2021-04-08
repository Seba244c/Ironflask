package dk.sebsa.ironflask.engine.gui;

import java.util.ArrayList;
import java.util.List;

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
	public boolean centered;
	public List<Modifier> modifiers = new ArrayList<>();
	public List<Animation> animations = new ArrayList<>();
	public float scale = 1f;
	
	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
	}
	
	public void prepare() {
		for(Animation animation : animations) {
			animation.prepare(this, rect);
		}
	}
	
	public void update() {		
		for(Animation animation : animations) {
			if(animation.isReady()) animation.tick(this, rect);
		}
	}
	
	public void calculateAnchors(Window window) {
		Vector2f sizeVector2f = size.calculate(window);
		Vector2f pos = posistion.calculate(window);
		rect = new Rect(0, 0, sizeVector2f.x, sizeVector2f.y);
		
		if(anchor == Anchor.BottomLeft) {
			rect.y = window.rect.height;
			rect.x += pos.x;
			rect.y -= pos.y+sizeVector2f.y;
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
		
		if(centered) {
			rect.x = rect.x + window.rect.width/2;
			rect.x -= rect.width/2;
		}
		if(rect.x % 1 != 0) rect.x += 0.5f;
		if(rect.y % 1 != 0) rect.y += 0.5f;
	}
	
	public void draw(Shader shader, Mesh2d mesh, Rect r) {
		Rect drawRect = r.copy();
		if(scale != 1) {
			float w = drawRect.width * scale;
			float h = drawRect.height * scale;
			
			drawRect.x += (drawRect.width-w)/2;
			drawRect.y += (drawRect.height-h)/2;
			
			drawRect.width = w;
			drawRect.height = h;
			
			if(drawRect.x % 1 != 0) drawRect.x += 0.5f;
			if(drawRect.y % 1 != 0) drawRect.y += 0.5f;
		}
		render(shader, mesh, drawRect);
	}
	
	public abstract void render(Shader shader, Mesh2d mesh, Rect r);
	public abstract boolean handleEvent(Event e);
}
