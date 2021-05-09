package dk.sebsa.ironflask.engine.core.events;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.ecs.World;

public class WorldChangedEvent extends Event {
	public final World newCurrentWorld;
	
	public WorldChangedEvent(EventType type, EventCatagory cat, World newWorld) {
		super(type, cat);
		this.newCurrentWorld = newWorld;
	}
}
