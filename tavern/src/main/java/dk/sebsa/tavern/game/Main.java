package dk.sebsa.tavern.game;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.tavern.game.layers.Debug;

public class Main {
	public static Application application;
	public static Debug debug;
	
	public static final boolean isDebug = true;
	
	public static void main(String[] args) {
		application = new Application("Project Tavern", true, Main::finishedLoading);
		
		// Adding all the layrs
		debug = new Debug(application);
		debug.setEnabled(false);
		
		if(isDebug) application.stack.addLayerToTop(debug);
		application.stack.addLayerToBot(new GameLayer(application));
		
		// vvvvvvvvvvvvvvv
		application.run();
	}
	
	public static void finishedLoading(Application app) {
		
	}
}
