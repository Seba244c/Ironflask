package dk.sebsa.ironflask.editor.ui;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.enums.Languages;
import dk.sebsa.ironflask.engine.gui.Constraint;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVar;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVector;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.SpriteSheet;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.gui.objects.ScrollableGuiList;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.local.LocalizationManager;

public class EditorLayer extends Layer {
	private Window inspectorWindow;
	private Window assetWindow;
	private Window worldWindow;
	private Application app;
	private Sprite window;
	
	public EditorLayer(Application app) {
		guiLayer = true;
		this.app = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			inspectorWindow.calculateConstraints(app);
			assetWindow.calculateConstraints(app);
			worldWindow.calculateConstraints(app);
		}
		
		if(assetWindow.handleEvent(e)) return true;
		if(worldWindow.handleEvent(e)) return true;
		return inspectorWindow.handleEvent(e);
	}

	@Override
	public void render() {
		app.guiRenderer.prepareForRender();
		
		app.guiRenderer.renderWindow(worldWindow);
		app.guiRenderer.renderWindow(assetWindow);
		app.guiRenderer.renderWindow(inspectorWindow);
		
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
		
		// Create window layout
		inspectorWindow	 = new Window(LocalizationManager.getString("gui.inspectorWindow.title"), window);
		worldWindow		 = new Window(LocalizationManager.getString("gui.worldWindow.title"), window);
		assetWindow		 = new Window(LocalizationManager.getString("gui.assetWindow.title"), window);
		
		worldWindow.addCosntraint(new Constraint(ConstraintSide.Bottom, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.3f)));
		worldWindow.addCosntraint(new Constraint(ConstraintSide.Right, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.8f)));
		assetWindow.addCosntraint(new Constraint(ConstraintSide.Top, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.7f)));
		assetWindow.addCosntraint(new Constraint(ConstraintSide.Right, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.2f)));
		inspectorWindow.addCosntraint(new Constraint(ConstraintSide.Left, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.8f)));
		
		ScrollableGuiList list = new ScrollableGuiList(worldWindow);
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
