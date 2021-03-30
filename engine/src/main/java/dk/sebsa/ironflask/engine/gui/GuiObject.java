package dk.sebsa.ironflask.engine.gui;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.gui.enums.GuiObjects;

public class GuiObject {
	public GuiObjects objectType;
	public List<Constraint> constraints = new ArrayList<>();
}
