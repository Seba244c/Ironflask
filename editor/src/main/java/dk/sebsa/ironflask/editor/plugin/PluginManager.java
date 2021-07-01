package dk.sebsa.ironflask.editor.plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginManager {
	public static List<Plugin> plugins = new ArrayList<>();
	
	private static List<Class<?>> getPluginClasses() {
		List<Class<?>> classes = new ArrayList<>();
		
//		Reflections reflections = new Reflections("my.project.prefix");

	}
	
	public static void fullInit() {
		
	}
}
