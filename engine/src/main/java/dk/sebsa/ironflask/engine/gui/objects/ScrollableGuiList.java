package dk.sebsa.ironflask.engine.gui.objects;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.MouseScrolledEvent;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.renderers.GuiRenderer;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Parent;
import dk.sebsa.ironflask.engine.math.Mathf;

public class ScrollableGuiList extends GuiList {
	private float scroll = 0f;
	private float maxScroll = 0;
	
	public ScrollableGuiList(Parent parent) {
		super(parent);
	}
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect rr, GuiRenderer renderer) {
		Rect oldRect = rect;
		rect = rr; 

		rect.y += scroll;
		maxScroll = rect.height;
		float contents = 0;
		
		for(GuiObject object : objects) {
			renderer.renderObject(object, this);
			rect.y += object.rect.height+padding;
			contents -= object.rect.height+padding;
		}
		
		maxScroll += contents;
		
		if(-contents <= rr.height) maxScroll = 0;
		
		rect = oldRect;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		super.handleEvent(e);
		
		if(e.type.equals(EventType.MouseScrolled)) {
			MouseScrolledEvent event = (MouseScrolledEvent) e;
			scroll += event.ofsetY*15;
			scroll = Mathf.clamp(scroll, maxScroll, 0);
		}
		
		return false;
	}
}
