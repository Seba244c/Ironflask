package dk.sebsa.ironflask.sandbox;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.events.Event;
import dk.sebsa.ironflask.engine.core.layer.Layer;

public class GameLayer extends Layer {
	private Application application;
	
	public GameLayer(Application app) {
		super();
		this.application = app;
		
	}
	@Override
	public boolean handleEvent(Event e) {
		
		return false;
	}
	
	@Override
	public void close() {
		
	}
	@Override
	public void render() {
		
	}
}
