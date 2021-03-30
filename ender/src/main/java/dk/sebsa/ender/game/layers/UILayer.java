package dk.sebsa.ender.game.layers;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.GUILayer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.gui.Constraint;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVar;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVector;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.enums.Anchor;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.math.Color;

public class UILayer extends GUILayer {
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
		
		if(pauseMenuEnabled) app.guiRenderer.renderWindow(pauseMenu);
		
		app.guiRenderer.endFrame();
	}

	@Override
	public void close() {
		
	}

	@Override
	public void init() {
		pauseMenu = new Window();
		pauseMenu.setBackgroundColor(Color.grey());
		pauseMenu.addCosntraint(new Constraint(ConstraintSide.Right, new GUIDynamicVar(GUIDynamicType.Dynamic, 0.7f)));
		
		GuiObject quitButton = new Button(app.input, this::quitButton);
		quitButton.material = new Material();
		quitButton.setAnchor(Anchor.TopLeft);
		quitButton.posistion =	new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 0.1f), new GUIDynamicVar(GUIDynamicType.Dynamic, 0.2f));
		quitButton.size = 		new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Dynamic, 0.8f), new GUIDynamicVar(GUIDynamicType.Fixed, 48));
		
		pauseMenu.addGuiObject(quitButton);
		pauseMenu.calculateConstraints(app);
	}
	
	private void quitButton(Button button) {
		app.close();
	}
}
