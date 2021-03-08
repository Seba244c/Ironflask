package dk.sebsa.ironflask.sandbox;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.sandbox.layers.Debug;
import dk.sebsa.ironflask.sandbox.layers.GameLayer;

public class Main {
	public static Debug debug;
	public static final boolean isDebug = true;
	
	public static void main(String[] args) {
		Application game = new Application("Ironflask Sandbox", isDebug);
		
		// Debug layer
		debug = new Debug(game);
		debug.enabled = false;
		
		// add layers
		game.stack.addLayerToBot(new GameLayer(game));
		if(isDebug) game.stack.addLayerToTop(debug);
		game.run();
	}
}
