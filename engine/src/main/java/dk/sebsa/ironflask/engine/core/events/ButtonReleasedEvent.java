package dk.sebsa.ironflask.engine.core.events;

import dk.sebsa.ironflask.engine.core.Event;

public class ButtonReleasedEvent extends Event {
	public ButtonReleasedEvent(EventType type, EventCatagory cat) {
		super(type, cat);
	}

	public int button;
}
