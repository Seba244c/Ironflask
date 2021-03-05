package dk.sebsa.ironflask.engine.core.events;

import dk.sebsa.ironflask.engine.core.Event;

public class MouseScrolledEvent extends Event {
	public MouseScrolledEvent(EventType type, EventCatagory cat) {
		super(type, cat);
	}
	public int ofsetX;
	public int ofsetY;
}
