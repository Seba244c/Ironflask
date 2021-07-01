package dk.sebsa.ironflask.editor.plugin.plugins.core;

import dk.sebsa.ironflask.editor.plugin.Plugin;

//@PluginRegister(ID="core", Name="Core", Author="Sebsa", Version="1.0.0")
public class Core extends Plugin {
	@Override public String ID() { return "core"; }
	@Override public String name() { return "Core"; }
	@Override public String author() { return "Sebsa"; }
	@Override public String version() { return "1.0.0"; }
}
