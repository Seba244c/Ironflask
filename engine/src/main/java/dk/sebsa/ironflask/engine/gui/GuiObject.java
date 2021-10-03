package dk.sebsa.ironflask.engine.gui;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.renderers.GuiRenderer;
import dk.sebsa.ironflask.engine.gui.enums.Anchor;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.math.Vector2f;

public abstract class GuiObject {
	private static final GUIDynamicVector DEFAULT_POS_VECTOR = new GUIDynamicVector(null, null);
	private static final GUIDynamicVector DEFAULT_SIZ_VECTOR = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 1), new GUIDynamicVar(GUIDynamicType.Fixed, 10));
	
	public Parent parent;
	
	public Rect rect = new Rect();
	public Sprite sprite;
	private static Sprite defualtSprite;
	private Anchor anchor;
	public GUIDynamicVector posistion;
	public GUIDynamicVector size;
	public boolean centered;
	public List<Modifier> modifiers = new ArrayList<>();
	public List<Animation> animations = new ArrayList<>();
	public float scale = 1f;
	
	public GuiObject(Parent parent) {
		this.parent = parent;
		this.posistion = DEFAULT_POS_VECTOR.clone();
		this.size = DEFAULT_SIZ_VECTOR.clone();

		parent.addGuiObject(this);
		
		if(defualtSprite == null) {
			defualtSprite = new Sprite(new Material(Color.red()));
		}
		this.sprite = defualtSprite;
	}
	
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
	
	public void calculateAnchors() {
		Vector2f sizeVector2f = size.calculate(parent);
		Vector2f pos = posistion.calculate(parent);
		rect.set(0, 0, sizeVector2f.x, sizeVector2f.y);
		
		if(anchor == Anchor.BottomLeft) {
			rect.y = parent.getRect().height;
			rect.x += pos.x;
			rect.y -= pos.y+sizeVector2f.y;
		} else if(anchor == Anchor.BottomMiddle) {
			rect.x = parent.getRect().width/2;
			rect.y = parent.getRect().height;
			rect.x += pos.x;
			rect.y -= pos.y;
		} else if(anchor == Anchor.BottomRight) {
			rect.x = parent.getRect().width;
			rect.y = parent.getRect().height;
			rect.x -= pos.x;
			rect.y -= pos.y;
		} else if(anchor == Anchor.LeftMiddle) {
			rect.y = parent.getRect().height / 2;
			rect.x += pos.x;
			rect.y += pos.y;
		} else if(anchor == Anchor.RightMiddle) {
			rect.y = parent.getRect().height / 2;
			rect.x = parent.getRect().width;
			rect.x -= pos.x;
			rect.y += pos.y;
		} else if(anchor == Anchor.TopLeft) {
			rect.x += pos.x;
			rect.y += pos.y;
		} else if(anchor == Anchor.TopRight) {
			rect.x = parent.getRect().width;
			rect.x -= pos.x;
			rect.y += pos.y;
		} else if(anchor == Anchor.TopMiddle) {
			rect.x = parent.getRect().width/2;
			rect.x += pos.x;
			rect.y += pos.y;
		}
		
		if(centered) {
			rect.x = rect.x + parent.getRect().width/2;
			rect.x -= rect.width/2;
		}
		if(rect.x % 1 != 0) rect.x += 0.5f;
		if(rect.y % 1 != 0) rect.y += 0.5f;
	}
	
	private Rect drawRect = new Rect();
	public void draw(Shader shader, Mesh2d mesh, Rect r, GuiRenderer renderer) {
		drawRect.set(r);
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
		render(shader, mesh, drawRect, renderer);
	}
	
	public abstract void render(Shader shader, Mesh2d mesh, Rect r, GuiRenderer renderer);
	public abstract boolean handleEvent(Event e);
}
