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
import dk.sebsa.ironflask.engine.graph.renderers.GuiRenderer;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Parent;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.math.Time;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class TextField extends GuiObject {
	public boolean isSelected = false;
	
	private Rect clickRect;
	private Input input;
	private Label label;
	private Sprite selected;
	private String text = "GE";
	private float anim;
	private int cursorPos = text.length()-1;
	
	public static Sprite cursorSprite = new Sprite("TextFieldCursor", new Material(Color.white()), new Rect(0,0,0,0), new Rect(0,0,0,0));
	
	public TextField(Parent parent, Sprite open, Sprite selected, Input input, Font font) {
		super(parent);
		this.sprite = open;
		this.selected = selected;
		this.input = input;
		this.label = new Label(text, font);
	}
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r, GuiRenderer renderer) {
		anim = draw(shader, mesh, r, sprite, selected, isSelected, label, anim, cursorPos);
		this.clickRect = r;
	}
	
	public static float draw(Shader shader, Mesh2d mesh, Rect r, Sprite open, Sprite selected, boolean isSelected, Label label, float anim, int cursorPos) {
		if(isSelected) {
			Box.draw(shader, mesh, r, selected);
			Text.draw(shader, mesh, r, label, false, 1f);
			
			// Cursor
			anim += 1*Time.getDeltaTime();
			if(anim >= 0) {
				Rect cursorRect = new Rect(r.x+getCursorX(label, cursorPos)+1, r.y+4, 3, r.height-8);
				if(anim >= 0.5f) anim = -0.5f;
				Box.draw(shader, mesh, cursorRect, cursorSprite);
			}
		} else {
			Box.draw(shader, mesh, r, open);
			Text.draw(shader, mesh, r, label, false, 1f);
		}
		
		return anim;
	}
	
	public static float getCursorX(Label label, int cursorPos) {
		if(label.getText().length()==0) return 0;
		String s = label.getText();
		s = s.substring(0, cursorPos+1);  
		return label.font.getStringWidth(s);
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
				anim = 0;
				text += (char) event.codePoint;
				cursorPos += 1;
				label.setText(text);
				limitCursorPos();
				return true;
			}
		}  else if (e.type == EventType.KeyPressed) {
			KeyPressedEvent event = (KeyPressedEvent) e;
			if(event.key == GLFW.GLFW_KEY_BACKSPACE) {
				if(text.length() == 0) return false;
				text = text.substring(0, text.length() - 1);  
				cursorPos -= 1;
				label.setText(text);
				limitCursorPos();
				return true;
			} else if(event.key == GLFW.GLFW_KEY_LEFT) {
				cursorPos -= 1;
				limitCursorPos();
				return true;
			} else if(event.key == GLFW.GLFW_KEY_RIGHT) {
				cursorPos += 1;
				limitCursorPos();
				return true;
			}
		}
		return false;
	}
	
	private void limitCursorPos() {
		if(cursorPos < -1) {
			cursorPos = 0;
		}
		else if(cursorPos > text.length()-1) cursorPos = text.length()-1;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		label.setText(text);
	}

}
