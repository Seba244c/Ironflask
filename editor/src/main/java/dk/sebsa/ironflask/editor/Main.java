package dk.sebsa.ironflask.editor;

import java.io.File;
import java.io.IOException;

import dk.sebsa.ironflask.editor.ui.EditorLayer;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.Entity;

public class Main {
	private static Application game;
	public static final boolean isDebug = true;
	
	public static void main(String[] args) throws IOException {
		if (parseArgs(args)) {
			Runtime.getRuntime().exec("../jdk-11.0.8/bin/java.exe -jar editor-launcher-0.0.1-SNAPSHOT.jar");
			return;
		}
		
		game = new Application("Ironflask Editor", isDebug, Main::finishedLoading);
		
		Entity e =new Entity("JE");
		new Entity("JE2").parent(e);
		new Entity("E");
		
		game.stack.addLayerToTop(new EditorLayer(game));
		
		game.run();
	}
	
	public static void finishedLoading(Application app) {
		
	}
	
	public static boolean parseArgs(String[] args) {
		for(String arg : args) {
			System.out.println("Arg: " + arg);
			if(arg == "launcher") return false;
		}
		
		return true;
	}
}
