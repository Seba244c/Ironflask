package dk.sebsa.ironflask.engine.core;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.io.LoggingUtil.Severity;

public class LayerStack {
	public String name;
	public Application app;
	public List<Layer> stack = new ArrayList<>();
	public List<Event> queue = new ArrayList<>();
	
	public LayerStack(Application app, String name) {
		this.app = app;
		this.name = name;
		LoggingUtil.appLog(app, Severity.Info, "Created LayerStack: "+name);
	}
	
	public void event(Event event) {
		queue.add(event);
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
			if(!e.handled && e.oneLayer) LoggingUtil.coreLog(Severity.Trace, name + " | "+"Unhandled event: " + e.toString());
		}
		queue.clear();
	}
	
	public void render() {
		LoggingUtil.coreLog(Severity.Trace, name + " | Recived render call");
		
		for(int i = 0; i < stack.size(); i++) {
			// Disabled Stacks
			if(!stack.get(i).enabled) return;
			stack.get(i).render();
		}
	}
	
	public void cleanup() {
		handleEvents();
		LoggingUtil.appLog(app, Severity.Info, "Cleanup LayerStack: "+name);
		for(Layer l : stack) {
			l.close();
		}
	}
}
