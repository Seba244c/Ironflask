package dk.sebsa.ironflask.engine.core;

import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;

public abstract class Layer {
	public String name = "";
	protected boolean enabled = true;
	public final boolean guiLayer;
	
	public abstract boolean handleEvent(Event e);
	public abstract void render();
	public abstract void close();
	public abstract void init();
	
	public Layer() {
		if(this.getClass().isAnnotationPresent(GUILayer.class)) guiLayer = true;
		else guiLayer = false;
	}
	
	public void setEnabled(boolean val) {
		enabled = val;
		if(!val) return;
		Event event = new Event(EventType.WindowResize, EventCatagory.Window);
        event.name = "Fake resize event";
        handleEvent(event);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
}
