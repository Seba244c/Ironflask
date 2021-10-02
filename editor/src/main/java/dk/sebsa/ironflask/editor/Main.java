package dk.sebsa.ironflask.editor;

import java.io.IOException;

import dk.sebsa.ironflask.editor.ui.EditorLayer;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.Entity;

public class Main {
	private static Application game;
	public static final boolean isDebug = true;
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static void main(String[] args) throws IOException {
		if (parseArgs(args)) {
			if(OS.indexOf("win") >= 0) Runtime.getRuntime().exec(".\\Ironflask Editor.exe");
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
			if(arg.equals("launcher")) return false;
		}
		
		return true;
	}
}
