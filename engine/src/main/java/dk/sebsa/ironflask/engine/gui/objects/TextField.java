package dk.sebsa.ironflask.engine.gui.objects;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.ButtonPressedEvent;
import dk.sebsa.ironflask.engine.core.events.CharEvent;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class TextField extends GuiObject {
	public boolean isSelected = false;
	
	private Rect clickRect;
	private Input input;
	private Label label;
	private Material selected;
	private String text = "GE";
	
	public TextField(Material open, Material selected, Input input, Font font) {
		this.material = open;
		this.selected = selected;
		this.input = input;
		this.label = new Label(text, font);
	}
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r) {
		draw(shader, mesh, r, material, selected, isSelected, label);
		this.clickRect = r;
	}
	
	public static void draw(Shader shader, Mesh2d mesh, Rect r, Material open, Material selected, boolean isSelected, Label label) {
		if(isSelected) {
			Box.draw(shader, mesh, r, selected);
			Text.draw(shader, mesh, r, label);
		} else {
			Box.draw(shader, mesh, r, open);
			Text.draw(shader, mesh, r, label);
		}
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.MouseButtonPressed) {
			ButtonPressedEvent event = (ButtonPressedEvent) e;
			
			if(event.button == 0) {
				if(clickRect.contains(new Vector2f(input.getMouseX(), input.getMouseY()))) {
					if(!isSelected) {
						isSelected = true;
						return true;
					}
				} else {
					if(isSelected) {
						isSelected = false;
						return true;
					}
				}
			}
		} else if (e.type == EventType.CharEvent) {
			CharEvent event = (CharEvent) e;
			if(isSelected) {
				text += (char) event.codePoint;
				label.setText(text);
				return true;
			}
		}  else if (e.type == EventType.KeyPressed) {
			KeyPressedEvent event = (KeyPressedEvent) e;
			if(event.key == GLFW.GLFW_KEY_BACKSPACE) {
				if(text.length() == 0) return false;
				text =text.substring(0, text.length() - 1);  
				label.setText(text);
				return true;
			}
		}
		return false;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		label.setText(text);
	}

}
