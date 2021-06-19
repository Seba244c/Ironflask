package dk.sebsa.ender.game.layers;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ender.game.Main;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.enums.Languages;
import dk.sebsa.ironflask.engine.gui.GUIXmlParser;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.annotaions.ButtonHandler;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.local.LocalizationManager;

public class MainMenu extends Layer {
	private Window entireScreen;
	private Application app;
	
	public MainMenu(Application app) {
		guiLayer = true;
		this.app = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			entireScreen.calculateConstraints(app);
		} else if(e.type == EventType.KeyPressed) {
			KeyPressedEvent event = (KeyPressedEvent) e;
			if(event.key == GLFW.GLFW_KEY_TAB) {
				if(Main.debug.isEnabled()) Main.debug.setEnabled(false);
				else Main.debug.setEnabled(true);
				return true;
			}
		}
		
		return entireScreen.handleEvent(e);
	}

	@Override
	public void render() {
		app.guiRenderer.prepareForRender();
		app.guiRenderer.renderWindow(entireScreen);
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
			entireScreen = GUIXmlParser.getWindow("mainmenu.xml", app);
			entireScreen.calculateConstraints(app);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@ButtonHandler(ID="mainmenu.play")
	public void play(Button button) {
		Main.mainMenu(false);
	}

	@ButtonHandler(ID="mainmenu.quit")
	public void quit(Button button) {
		app.close();
	}
}
