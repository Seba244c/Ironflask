package dk.sebsa.ironflask.sandbox.layers;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.sandbox.Main;

public class GameLayer extends Layer {
	private Application application;
	
	public GameLayer(Application app) {
		super();
		this.application = app;
		
	}
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.KeyPressed) {
			KeyPressedEvent e2 = (KeyPressedEvent) e;
			if(e2.key == GLFW.GLFW_KEY_TAB) {
				if(Main.debug.enabled) Main.debug.enabled = false;
				else Main.debug.enabled = true;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void close() {
		
	}
	
	@Override
	public void render() {
		
	}
}
