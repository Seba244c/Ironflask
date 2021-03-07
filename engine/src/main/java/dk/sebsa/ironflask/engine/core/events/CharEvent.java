package dk.sebsa.ironflask.engine.core.events;

import dk.sebsa.ironflask.engine.core.Event;

public class CharEvent extends Event {
	public CharEvent(EventType type, EventCatagory catagory) {
		this.type = type;
		this.catagory = catagory;
	}

	public int codePoint;
}
