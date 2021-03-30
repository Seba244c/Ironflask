package dk.sebsa.ender.game.layers;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.gui.Constraint;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintSide;
import dk.sebsa.ironflask.engine.gui.enums.ConstraintType;
import dk.sebsa.ironflask.engine.math.Color;

public class GUILayer extends Layer {
	public Application app;
	
	public boolean pauseMenuEnabled;
	private Window pauseMenu;
	
	public GUILayer(Application app) {
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
				return true;
			}
		}
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
		pauseMenu.addCosntraint(new Constraint(ConstraintSide.Right, ConstraintType.Dynamic, 0.6f));
		pauseMenu.calculateConstraints(app);
	}
}
