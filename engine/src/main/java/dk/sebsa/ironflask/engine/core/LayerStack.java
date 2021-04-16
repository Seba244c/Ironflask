package dk.sebsa.ironflask.engine.core;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.enums.*;

public class LayerStack {
	public String name;
	public Application app;
	private List<Layer> stack = new ArrayList<>();
	private List<Event> queue = new ArrayList<>();
	
	public LayerStack(Application app, String name) {
		this.app = app;
		this.name = name;
		LoggingUtil.coreLog(Severity.Info, "Created LayerStack: "+name);
	}
	
	public void addLayerToTop(Layer layer) {
    	LoggingUtil.coreLog(Severity.Info, "Add layer on top: " + layer.name);
		stack.add(layer);
	}
	
	public void addLayerToBot(Layer layer) {
    	LoggingUtil.coreLog(Severity.Info, "Add layer on bot: " + layer.name);
		stack.add(0, layer);
	}
	
	public void insertLayer(Layer layer, int index) {
    	LoggingUtil.coreLog(Severity.Info, "Inserted layer: " + layer.name);
		stack.add(index, layer);
	}
	
	public void event(Event event) {
		queue.add(event);
	}
	
	public void init() {
		LoggingUtil.coreLog(Severity.Info, "Initlializing LayerStack: "+name);
		for(Layer layer : stack) {
			layer.init();
		}
	}
	
	public void handleEvents() {
		for(int i = 0; i < queue.size(); i++) {
			Event e = queue.get(i);
			for(int a = stack.size()-1; a > -1; a--) {
				Layer layer = stack.get(a);
				// Disabled Layers
				if(!layer.enabled) continue;
				
				// Handle
				e.handled = layer.handleEvent(e);
				if(e.handled && e.oneLayer) break;
			}
		}
		queue.clear();
	}
	
	public void render() {		
		for(int i = 0; i < stack.size(); i++) {
			// Return if disabled or if an GUI layer
			Layer layer = stack.get(i);
			if(!layer.enabled || layer.guiLayer) continue;
			layer.render();
		}
	}
	
	public void renderGUI() {
		for(int i = 0; i < stack.size(); i++) {
			// Return if disabled or not GUI layer
			Layer layer = stack.get(i);
			if(!layer.enabled || !layer.guiLayer) continue;
			layer.render();
		}
	}
	
	public void cleanup() {
		try {
			handleEvents();
			LoggingUtil.coreLog(Severity.Info, "Cleanup LayerStack: "+name);
			for(Layer l : stack) {
				l.close();
			}
		} catch (Exception e) {
			// NOTHING
		}
	}
}
