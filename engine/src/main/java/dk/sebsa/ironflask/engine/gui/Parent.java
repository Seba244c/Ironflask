package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.graph.Rect;

public interface Parent {
	public Rect getRect();
	public void addGuiObject(GuiObject object);
}
