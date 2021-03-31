package dk.sebsa.ender.game.layers;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.gui.Constraint;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVar;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVector;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.enums.Anchor;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.objects.Box;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.gui.objects.TextField;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;

public class UILayer extends Layer {
	public Application app;
	
	public boolean pauseMenuEnabled;
	private Window pauseMenu;
	private Window entireScreen;
	
	private Font buttonFont;
	
	public UILayer(Application app) {
		this.app = app;
		this.guiLayer = true;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			pauseMenu.calculateConstraints(app);
		} else if(e.type == EventType.KeyPressed) {
			KeyPressedEvent event = (KeyPressedEvent) e;
			if(event.key == GLFW.GLFW_KEY_ESCAPE) {
				if(pauseMenuEnabled) pauseMenuEnabled = false;
				else pauseMenuEnabled = true;
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
		
		app.guiRenderer.renderWindow(entireScreen);
		if(pauseMenuEnabled) app.guiRenderer.renderWindow(pauseMenu);
		
		app.guiRenderer.endFrame();
	}

	@Override
	public void close() {
		
	}

	@Override
	public void init() {
		// Font
		try {
			buttonFont = new Font(new java.awt.Font("OpenSans", java.awt.Font.BOLD, 42));
		} catch (AssetExistsException e) { e.printStackTrace(); }
		
		// Menus
		pauseMenu = new Window();
		pauseMenu.setBackgroundColor(Color.grey());
		pauseMenu.addCosntraint(new Constraint(ConstraintSide.Right, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.7f)));
		entireScreen = new Window();
		entireScreen.setBackgroundColor(Color.transparent());
		
		// GUI Dynamic consts
		GUIDynamicVar size48 = new GUIDynamicVar(GUIDynamicType.Fixed, 48);
		
		// Quit Button
		GuiObject guiObject = new Button(app.input, this::quitButton, new Label("Quit", buttonFont), true);
		guiObject.material = new Material();
		guiObject.setAnchor(Anchor.TopLeft);
		guiObject.posistion =	new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 0.1f), new GUIDynamicVar(GUIDynamicType.Dynamic, 0.2f));
		guiObject.size = 		new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 0.8f), size48);
		pauseMenu.addGuiObject(guiObject);
		
		// Compass
		guiObject = new Box();
		guiObject.material = new Material(Texture.getTexture("compass.png"));
		guiObject.setAnchor(Anchor.TopLeft);
		guiObject.posistion =	new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 0.1f), new GUIDynamicVar(GUIDynamicType.Dynamic, 0.2f));
		guiObject.size = 		new GUIDynamicVector(size48, size48);
		entireScreen.addGuiObject(guiObject);
		
		// Text
		Material open = new Material();
		Material selected = new Material();
		selected.setColor(Color.cyan());
		TextField textField = new TextField(open, selected, app.input, buttonFont);
		textField.setAnchor(Anchor.TopLeft);
		textField.posistion =	new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 0.1f), new GUIDynamicVar(GUIDynamicType.Fixed, 10f));
		textField.size = 		new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 0.8f), size48);
		pauseMenu.addGuiObject(textField);

		entireScreen.calculateConstraints(app);
		pauseMenu.calculateConstraints(app);
	}
	
	private void quitButton(Button button) {
		app.close();
	}
}
