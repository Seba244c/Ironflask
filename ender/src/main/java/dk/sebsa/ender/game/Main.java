package dk.sebsa.ender.game;

import dk.sebsa.ender.game.layers.Debug;
import dk.sebsa.ender.game.layers.EnderGame;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.math.Color;

public class Main {
	public static Debug debug;
	public static final boolean isDebug = true;
	
	public static void main(String[] args) {
		Application game = new Application("Project Ender", isDebug);
		game.window.setClearColor(Color.forest());
		
		// Debug layer
		debug = new Debug(game);
		debug.enabled = false;
		
		// add layers
		game.stack.addLayerToBot(new EnderGame(game));
		if(isDebug) game.stack.addLayerToTop(debug);
		game.run();
	}
}
