package dk.sebsa.ender.game.layers;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ender.game.Main;
import dk.sebsa.ender.game.MusicManager;
import dk.sebsa.ender.game.MusicManager.Songs;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.GUILayer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.enums.Languages;
import dk.sebsa.ironflask.engine.gui.GUIXmlParser;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.annotaions.ButtonHandler;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.local.LocalizationManager;

/**
 * @author seba2
 *
 */
@GUILayer
public class UILayer extends Layer {
	public Application app;
	
	public boolean pauseMenuEnabled;
	public boolean settingsMenuEnabled;
	private Window pauseMenu;
	private Window settingsMenu;
		
	public UILayer(Application app) {
		this.app = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			settingsMenu.calculateConstraints(app);
			pauseMenu.calculateConstraints(app);
		} else if(e.type == EventType.KeyPressed) {
			KeyPressedEvent event = (KeyPressedEvent) e;
			if(event.key == GLFW.GLFW_KEY_ESCAPE) {
				if(settingsMenuEnabled) settingsMenuEnabled = false;
				else if(pauseMenuEnabled) {
					pauseMenuEnabled = false;
					MusicManager.start(Songs.Game);
				} else {
					MusicManager.start(Songs.MainMenu);
					pauseMenuEnabled = true;
				}
				app.pauseLogic(pauseMenuEnabled);
				return true;
			}
			
		}
		if(settingsMenuEnabled) return settingsMenu.handleEvent(e);
		else if(pauseMenuEnabled) return pauseMenu.handleEvent(e);
		return false;
	}

	@Override
	public void render() {
		app.guiRenderer.prepareForRender();
		
		if(pauseMenuEnabled) app.guiRenderer.renderWindow(pauseMenu);
		if(settingsMenuEnabled) app.guiRenderer.renderWindow(settingsMenu);
		
		app.guiRenderer.endFrame();
	}

	@Override
	public void close() {
		
	}

	@Override
	public void init() {	
		LocalizationManager.setLangauage(Languages.en);
		
		try {
			GUIXmlParser.setupButtons(getClass(), this);
			pauseMenu = GUIXmlParser.getWindow("pausemenu.xml", app);
			pauseMenu.calculateConstraints(app);
			settingsMenu = GUIXmlParser.getWindow("pausemenu.xml", app);
			settingsMenu.calculateConstraints(app);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@ButtonHandler(ID="pausemenu.quit")
	public void quitButton(Button button) {
		Main.mainMenu(true);
		pauseMenuEnabled = false;
		settingsMenuEnabled = false;
	}
	
	@ButtonHandler(ID="pausemenu.settings")
	public void settingsButton(Button button) {
		settingsMenuEnabled = true;
	}
}
