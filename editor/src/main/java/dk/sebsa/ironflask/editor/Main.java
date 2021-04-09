package dk.sebsa.ironflask.editor;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.math.Color;

public class Main {
	private static Application game;
	public static final boolean isDebug = true;
	
	public static void main(String[] args) {
		game = new Application("Ironflask Editor", isDebug, Main::finishedLoading);
		game.setClearColor(Color.forest());
		game.run();
	}
	
	public static void finishedLoading(Application app) {
		
	}
}
