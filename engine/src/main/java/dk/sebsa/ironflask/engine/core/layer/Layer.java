package dk.sebsa.ironflask.engine.core.layer;

import dk.sebsa.ironflask.engine.core.events.Event;

public abstract class Layer {
	public String name = "";
	public boolean enabled = true;
	
	public abstract boolean handleEvent(Event e);
	public abstract void render();
	public abstract void close();
}
