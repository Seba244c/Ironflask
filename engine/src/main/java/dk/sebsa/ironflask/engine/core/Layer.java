package dk.sebsa.ironflask.engine.core;

public abstract class Layer {
	public String name = "";
	public boolean enabled = true;
	public boolean guiLayer = false;
	
	public abstract boolean handleEvent(Event e);
	public abstract void render();
	public abstract void close();
	public abstract void init();
}
