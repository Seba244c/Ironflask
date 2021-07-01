package dk.sebsa.ironflask.editor.plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;

public class PluginManager {
	public static List<Plugin> plugins = new ArrayList<>();
	
	private static List<Class<? extends Plugin>> getPluginClasses() {
		List<Class<?extends Plugin>> classes = new ArrayList<>();
		
		Reflections reflections = new Reflections(ClasspathHelper.forJavaClassPath(), new SubTypesScanner());
		Set<Class<? extends Plugin>> pluginsClasses = reflections.getSubTypesOf(Plugin.class);

		classes.addAll(pluginsClasses);
		
		return classes;
	}
	
	private static List<Class<? extends Module>> getModuleClasses() {
		List<Class<?extends Module>> classes = new ArrayList<>();
		
		Reflections reflections = new Reflections(ClasspathHelper.forJavaClassPath(), new SubTypesScanner());
		Set<Class<? extends Module>> clzs = reflections.getSubTypesOf(Module.class);
		
		for(Class<? extends Module> clz : clzs) {
			if(clz.isAnnotationPresent(PluginModule.class)) classes.add(clz);
		}
		
		return classes;
	}
	
	private static void setupPlugin(Plugin plugin, Application app, Map<String, List<Class<? extends Module>>> modules) {
		LoggingUtil.appLog(app, Severity.Info, "Loadeding plugin: " + plugin.toString());
		if(plugin.ID() == "core" && plugin.author() == "Sebsa") plugin.core = true;
		
		// Modules
		List<Class<? extends Module>> pluginModules = modules.get(plugin.ID());
		if(pluginModules != null) {
			for(Class<? extends Module> module : pluginModules) {
				try {
					Module actualNewModule = module.getConstructor().newInstance();
					plugin.modules.add(actualNewModule);
					
					LoggingUtil.appLog(app, Severity.Info, "  - " + actualNewModule.toString());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void fullInit(Application app) {
		// Find modules
		List<Class<?extends Module>> clzs = getModuleClasses();
		Map<String, List<Class<? extends Module>>> modules = new HashMap<>();
		
		for(Class<? extends Module> module : clzs) {
			PluginModule an = module.getAnnotation(PluginModule.class);
			
			if(!modules.containsKey(an.pluginID())) {
				modules.put(an.pluginID(), new ArrayList<>());
			} modules.get(an.pluginID()).add(module);
		}
		
		// Load plugins
		List<Class<?extends Plugin>> classes = getPluginClasses();
		
		for (Class<? extends Plugin> clz : classes) {
			try {
				Plugin plugin = clz.getConstructor().newInstance();
				
				// Setup Plugin
				setupPlugin(plugin, app, modules);
				
				plugins.add(plugin);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}
