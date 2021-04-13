package dk.sebsa.ironflask.editor.ui;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVar;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVector;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.SpriteSheet;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.objects.Box;

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
		
		return false;
	}

	@Override
	public void render() {
		app.guiRenderer.prepareForRender();
		
		app.guiRenderer.renderWindow(testWindow, window);
		
		app.guiRenderer.endFrame();
	}

	@Override
	public void close() {
		
	}

	@Override
	public void init() {
		testWindow = new Window("HELLO");
		SpriteSheet sheet = SpriteSheet.getSheet("Ironflask_BlackGUI");
		window = sheet.getSprite("Window");
		
		Box testBox = new Box();
		testBox.sprite = sheet.getSprite("Box");
		testBox.size = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 0.5f), new GUIDynamicVar(GUIDynamicType.Dynamic, 0.5f));
		testWindow.addGuiObject(testBox);
	}
}
