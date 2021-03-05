package dk.sebsa.ironflask.sandbox;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.sandbox.debug.ImGuiLayer;

public class Main {
	public static void main(String[] args) {
		Application game = new Application("Ironflask Sandbox");
		game.stack.stack.add(new GameLayer(game));
		game.stack.stack.add(new ImGuiLayer(game));
		game.run();
	}
}
