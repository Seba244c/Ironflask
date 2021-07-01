package dk.sebsa.ironflask.editor.plugin;

public class Module {
	@Override
	public String toString() {
		PluginModule anno = this.getClass().getAnnotation(PluginModule.class);
		
		return "<Module ID=" + anno.ID()+">";
	}
}
