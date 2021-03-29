package dk.sebsa.ironflask.engine.core.events;

import dk.sebsa.ironflask.engine.core.Event;

public class MouseMoveEvent extends Event {
	public MouseMoveEvent(EventType type, EventCatagory cat) {
		super(type, cat);
	}
	public int[] mousePosX = new int[1];
	public int[] mousePosY = new int[1];
	public int[] offsetPosX = new int[1];
	public int[] offsetPosY = new int[1];
}
