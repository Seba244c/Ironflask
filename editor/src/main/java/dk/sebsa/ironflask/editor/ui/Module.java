package dk.sebsa.ironflask.editor.ui;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.gui.Constraint;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.Window;

public abstract class Module {
	public Window guiWindow;
	public Application app;
	public EditorLayer editor;
	
	public Module(String title, Sprite window, EditorLayer editor, Application app) {
		guiWindow = new Window(title, window);
		this.editor = editor;
		this.app = app;
	}
	
	public abstract void init();
	
	public void render(Application app) {
		app.guiRenderer.renderWindow(guiWindow);	
	}
	
	public boolean handleEvent(Event e) {
		return guiWindow.handleEvent(e);
	}
	
	public void addCosntraint(Constraint c) {
		guiWindow.addCosntraint(c);
	}
	
	public void calculateConstraints() {
		guiWindow.calculateConstraints(app);
	}
	
	public void calculateConstraints(Application a) {
		guiWindow.calculateConstraints(a);
	}
}
