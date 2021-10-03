package dk.sebsa.ironflask.launcher;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.GUILayer;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.enums.Languages;
import dk.sebsa.ironflask.engine.gui.GUIXmlParser;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.annotaions.ButtonHandler;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.local.LocalizationManager;

@GUILayer
public class UILayer extends Layer {
	private Application application;
	private int state = -420;
	
	private Window windowWelcome;
	private Window windowConfig;
	private Window windowProjects;
	
	private Window currentWindow;
	
	public UILayer(Application app) {
		this.application = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {		
		if(e.type == EventType.WindowResize) {
			currentWindow.calculateConstraints(application);
		}
		
		return currentWindow.handleEvent(e);
	}

	@Override
	public void render() {
		application.guiRenderer.prepareForRender();
		
		application.guiRenderer.renderWindow(currentWindow);
		
		application.guiRenderer.endFrame();
	}
	
	public Window getCorrectWindow() {
		switch (state) {
			case -2:
				return windowWelcome;
			case -1:
				return windowConfig;
			case 0:
				return windowProjects;
		}
		
		return windowWelcome;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void changeState(int newState) {
		state = newState;
		currentWindow = getCorrectWindow();
		currentWindow.calculateConstraints(application);
	}

	@Override
	public void init() {
		LocalizationManager.setLangauage(Languages.en);
		
		try {
			GUIXmlParser.setupButtons(getClass(), this);
			windowWelcome = GUIXmlParser.getWindow("launcher_welcome.xml", application);
			windowConfig = GUIXmlParser.getWindow("launcher_config.xml", application);
			windowProjects = GUIXmlParser.getWindow("launcher_projects.xml", application);
		} catch (Exception e) { e.printStackTrace(); }
		
		// Set correct window
		ConfigManager.init();
		if(ConfigManager.newUsr) {
			System.out.println("NEW USER!");
			changeState(-2);
		} else {
			System.out.println("OLD USER!");
			changeState(0);
		}
	}
	
	@ButtonHandler(ID="launcher.welcome.start")
	public void buttonStart(Button button) {
		changeState(-1);
	}
}
