package dk.sebsa.ironflask.engine.core.events;

import dk.sebsa.ironflask.engine.core.Event;

public class ButtonPressedEvent extends Event {
	public ButtonPressedEvent(EventType type, EventCatagory cat) {
		super(type, cat);
	}

	public int button;
}
