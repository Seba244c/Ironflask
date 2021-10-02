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
	private int state = -1;
	
	private Window windowWelcome;
	
	public UILayer(Application app) {
		this.application = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {		
		if(e.type == EventType.WindowResize) {
			windowWelcome.calculateConstraints(application);
		}
		
		return windowWelcome.handleEvent(e);
	}

	@Override
	public void render() {
		application.guiRenderer.prepareForRender();
		
		switch (state) {
			case -1:
				application.guiRenderer.renderWindow(windowWelcome);
				break;
		}
		
		application.guiRenderer.endFrame();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		LocalizationManager.setLangauage(Languages.en);
		
		try {
			GUIXmlParser.setupButtons(getClass(), this);
			windowWelcome = GUIXmlParser.getWindow("launcher_welcome.xml", application);
			windowWelcome.calculateConstraints(application);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@ButtonHandler(ID="launcher.welcome.start")
	public void buttonStart(Button button) {
		Main.runEngine();
		application.close();
	}
}
