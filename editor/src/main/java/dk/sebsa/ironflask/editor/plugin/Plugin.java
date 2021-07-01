package dk.sebsa.ironflask.editor.plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class Plugin {
	public abstract String ID();
	public abstract String name();
	public abstract String author();
	public abstract String version();
	
	protected boolean core = false;
	protected boolean enabled = true;
	
	protected List<Module> modules = new ArrayList<>();
	
	@Override
	public String toString() {
		return "<Plugin ID=" + ID() + " version="+version()+">";
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		if (core) return;
		this.enabled = enabled;
	}
}
