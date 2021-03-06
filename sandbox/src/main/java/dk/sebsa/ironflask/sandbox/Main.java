package dk.sebsa.ironflask.sandbox;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.sandbox.layers.Debug;
import dk.sebsa.ironflask.sandbox.layers.GameLayer;

public class Main {
	public static Debug debug;
	
	public static void main(String[] args) {
		Application game = new Application("Ironflask Sandbox");
		
		// Debug layer
		debug = new Debug(game);
		debug.enabled = false;
		
		// add layers
		game.stack.stack.add(new GameLayer(game));
		game.stack.stack.add(debug);
		game.run();
	}
}
