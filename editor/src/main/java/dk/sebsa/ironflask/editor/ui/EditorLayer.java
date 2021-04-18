package dk.sebsa.ironflask.editor.ui;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.enums.Languages;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVar;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVector;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.SpriteSheet;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.gui.objects.ScrollableGuiList;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.local.LocalizationManager;

public class EditorLayer extends Layer {
	private Window testWindow;
	private Application app;
	private Sprite window;
	
	public EditorLayer(Application app) {
		guiLayer = true;
		this.app = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			testWindow.calculateConstraints(app);
		}
		
		return testWindow.handleEvent(e);
	}

	@Override
	public void render() {
		app.guiRenderer.prepareForRender();
		
		app.guiRenderer.renderWindow(testWindow);
		
		app.guiRenderer.endFrame();
	}

	@Override
	public void close() {
		
	}

	@Override
	public void init() {
		Font font = Font.getFont(new java.awt.Font("OpenSans", 0, 48));
		LocalizationManager.setLangauage(Languages.en);
		
		SpriteSheet sheet = SpriteSheet.getSheet("Ironflask_BlackGUI");
		window = sheet.getSprite("Window");
		testWindow = new Window(LocalizationManager.getString("gui.testWindow.title"), window);
		
		ScrollableGuiList list = new ScrollableGuiList(testWindow);
		list.size = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Fixed, font.getStringWidth("9")+4), new GUIDynamicVar(GUIDynamicType.Dynamic, 1f));
		
		for(int i = 0; i<10; i++) {
			Button testBox = new Button(list, app.input, this::buttonClick, new Label(String.valueOf(i), font), false);
			testBox.sprite = sheet.getSprite("Box");
			testBox.size = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 1f), new GUIDynamicVar(GUIDynamicType.Fixed, 52.0f));
		}
		
		Button testBox = new Button(list, app.input, this::buttonClick, new Label("E", font), false);
		testBox.sprite = sheet.getSprite("Box");
		testBox.size = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 1f), new GUIDynamicVar(GUIDynamicType.Fixed, 52.0f));
	}
	
	private void buttonClick(Button button) {
		System.out.println(button.label.getText());
	}
}
