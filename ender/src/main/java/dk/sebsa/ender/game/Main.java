package dk.sebsa.ender.game;

import dk.sebsa.ender.game.MusicManager.Songs;
import dk.sebsa.ender.game.layers.Debug;
import dk.sebsa.ender.game.layers.EnderGame;
import dk.sebsa.ender.game.layers.MainMenu;
import dk.sebsa.ender.game.layers.UILayer;
import dk.sebsa.ender.game.skill.SkillManager;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.World;
import dk.sebsa.ironflask.engine.ecs.WorldManager;

public class Main {
	public static Debug debug;
	public static EnderGame enderGame;
	public static UILayer UILayer;
	public static MainMenu mainmenu;
	
	public static final boolean isDebug = true;
	private static Application game;
	
	public static World mainMenuWorld = new World();
	public static World testWorld = new World();
	
	public static void main(String[] args) {
		game = new Application("Project Ender", isDebug, Main::loadingFinished, Main::load);
		
		// Set world
		WorldManager.setWorld(mainMenuWorld, game);
		
		// Layers
		debug = new Debug(game);
		debug.setEnabled(false);
		
		UILayer = new UILayer(game);
		UILayer.setEnabled(false);
		
		enderGame = new EnderGame(game);
		enderGame.setEnabled(false);
		
		mainmenu = new MainMenu(game);
		
		// add layers
		game.stack.addLayerToBot(enderGame);
		game.stack.addLayerToTop(UILayer);
		game.stack.addLayerToTop(mainmenu);
		if(isDebug) game.stack.addLayerToTop(debug);
		game.run();
	}
	
	public static void mainMenu(boolean on) {
		if(on) {
			UILayer.setEnabled(false);
			enderGame.setEnabled(false);
			mainmenu.setEnabled(true);
			WorldManager.setWorld(mainMenuWorld, game);
			MusicManager.start(Songs.MainMenu);
		} else {
			UILayer.setEnabled(true);
			enderGame.setEnabled(true);
			mainmenu.setEnabled(false);
			WorldManager.setWorld(testWorld, game);
			MusicManager.start(Songs.Game);
		}
	}
	
	public static void load(Application app) {
		SkillManager.init();
	}
	
	public static void loadingFinished(Application app) {
		// Audio
		MusicManager.init();
		MusicManager.start(Songs.MainMenu);
	}
}
