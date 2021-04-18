package dk.sebsa.ironflask.engine.gui.objects;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.renderers.GuiRenderer;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Parent;

public class GuiList extends GuiObject implements Parent {
	public float padding = 2;
	public GuiList(Parent parent) {
		super(parent);
	}

	public List<GuiObject> objects = new ArrayList<>();
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect rr, GuiRenderer renderer) {
		Rect oldRect = rect;
		rect = rr;
		for(GuiObject object : objects) {
			renderer.renderObject(object, this);
			rect.y += object.rect.height+padding;
		}
		rect = oldRect;
	}

	@Override
	public boolean handleEvent(Event e) {
		for(GuiObject object : objects) {
			if(object.handleEvent(e)) return true;
		}
		
		return false;
	}
	
	@Override
	public void calculateAnchors() {
		super.calculateAnchors();
		for(GuiObject object : objects) {
			object.calculateAnchors();
		}
	}
	
	@Override
	public Rect getRect() {
		return rect;
	}

	@Override
	public void addGuiObject(GuiObject object) {
		objects.add(object);
	}

}
