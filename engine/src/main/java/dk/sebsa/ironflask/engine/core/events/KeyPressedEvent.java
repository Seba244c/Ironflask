package dk.sebsa.ironflask.engine.core.events;

import dk.sebsa.ironflask.engine.core.Event;

public class KeyPressedEvent extends Event {
	public KeyPressedEvent(EventType type, EventCatagory cat) {
		super(type, cat);
	}

	public int key;
}
