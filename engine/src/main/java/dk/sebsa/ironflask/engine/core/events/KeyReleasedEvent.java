package dk.sebsa.ironflask.engine.core.events;

import dk.sebsa.ironflask.engine.core.Event;

public class KeyReleasedEvent extends Event {
	public KeyReleasedEvent(EventType type, EventCatagory cat) {
		super(type, cat);
	}
	
	public int key;
}
