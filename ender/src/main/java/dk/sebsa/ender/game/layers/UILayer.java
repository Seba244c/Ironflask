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
	private Window pauseMenu;
		
	public UILayer(Application app) {
		this.app = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			pauseMenu.calculateConstraints(app);
		} else if(e.type == EventType.KeyPressed) {
			KeyPressedEvent event = (KeyPressedEvent) e;
			if(event.key == GLFW.GLFW_KEY_ESCAPE) {
				if(pauseMenuEnabled) {
					pauseMenuEnabled = false;
					MusicManager.start(Songs.Game);
				}
				else {
					MusicManager.start(Songs.MainMenu);
					pauseMenu.calculateConstraints(app);
					pauseMenuEnabled = true;
				}
				app.pauseLogic(pauseMenuEnabled);
				return true;
			}
		}
		if(pauseMenuEnabled) return pauseMenu.handleEvent(e);
		return false;
	}

	@Override
	public void render() {
		app.guiRenderer.prepareForRender();
		
		if(pauseMenuEnabled) app.guiRenderer.renderWindow(pauseMenu);
		
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
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@ButtonHandler(ID="pausemenu.quit")
	public void quitButton(Button button) {
		Main.mainMenu(true);
		pauseMenuEnabled = false;
	}
}
