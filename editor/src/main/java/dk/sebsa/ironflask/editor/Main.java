package dk.sebsa.ironflask.editor;

import dk.sebsa.ironflask.editor.ui.EditorLayer;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.Entity;

public class Main {
	private static Application game;
	public static final boolean isDebug = true;
	
	public static void main(String[] args) {
		game = new Application("Ironflask Editor", isDebug, Main::finishedLoading);
		
		Entity e =new Entity("JE");
		new Entity("JE2").parent(e);
		new Entity("E");
		
		game.stack.addLayerToTop(new EditorLayer(game));
		
		game.run();
	}
	
	public static void finishedLoading(Application app) {
		
	}
}
