package dk.sebsa.ironflask.engine.core.events;

import dk.sebsa.ironflask.engine.core.layer.LayerStack;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.io.LoggingUtil.Severity;

public class Event {
	public enum EventType {
		WindowClose, WindowResize, WindowMinimize, WindowUnMinimize, WindowMoved,
		AppUpdate, AppRender,
		KeyPressed, KeyReleased,
		MouseButtonPressed, MouseButtonReleased, MouseMoved, MouseScrolled
	}
	
	public enum EventCatagory {
		Window, Mouse, App
	}
	
	public String name;
	public EventType type;
	public EventCatagory catagory;
	public boolean handled = false;
	public boolean oneLayer = true;
	
	public Event() {
		
	}
	
	public Event(EventType type, EventCatagory catagory) {
		this.type = type;
		this.catagory = catagory;
	}
	
	public void dispatch(LayerStack stack) {
		if(type == null) {
			LoggingUtil.coreLog(Severity.Error, "Event type is not defined");
		} else {
			stack.event(this);
		}
	}
	
	public String toString() {
		String s = "";
		if(name != null) s+=name+" | ";
		s += type.toString();
		
		return s;
	}
}
