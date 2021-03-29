package dk.sebsa.ironflask.engine.ecs;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.ButtonPressedEvent;
import dk.sebsa.ironflask.engine.core.events.ButtonReleasedEvent;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.core.events.KeyReleasedEvent;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.io.Window;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class ComponentInput {
	private Input input;
	public Window window;
	
	private byte[] keys;
	private byte[] keysPressed;
	private byte[] keysReleased;
	private byte[] buttons;
	private byte[] buttonsPressed;
	private byte[] buttonsReleased;
	
	public ComponentInput(Input input) {
    	LoggingUtil.coreLog(Severity.Trace, "Creating ComponentInput");
		this.input = input;
		this.window = input.window;
		
		keys = new byte[GLFW.GLFW_KEY_LAST];
		keysPressed = new byte[GLFW.GLFW_KEY_LAST];
		keysReleased = new byte[GLFW.GLFW_KEY_LAST];
		buttons = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
		buttonsPressed = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
		buttonsReleased = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
	}
	
	public boolean onEvent(Event e) {
		if(e.catagory != EventCatagory.Input && e.type != EventType.AppLate) return false;
		
		if(e.type == EventType.AppLate) {
			keysPressed = new byte[GLFW.GLFW_KEY_LAST];
			keysReleased = new byte[GLFW.GLFW_KEY_LAST];
			buttonsPressed = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
			buttonsReleased = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
		}
		
		// Keys
		else if(e.type == EventType.KeyPressed) {
			KeyPressedEvent e2 = (KeyPressedEvent) e;
			keys[e2.key] = 1;
			keysPressed[e2.key] = 1;
		} else if(e.type == EventType.KeyReleased) {
			KeyReleasedEvent e2 = (KeyReleasedEvent) e;
			keys[e2.key] = 0;
			keysReleased[e2.key] = 1;
		}
		
		// Button
		else if(e.type == EventType.MouseButtonPressed) {
			ButtonPressedEvent e2 = (ButtonPressedEvent) e;
			buttons[e2.button] = 1;
			buttonsPressed[e2.button] = 1;
		} else if(e.type == EventType.MouseButtonReleased) {
			ButtonReleasedEvent e2 = (ButtonReleasedEvent) e;
			buttons[e2.button] = 0;
			buttonsReleased[e2.button] = 1;
		}
		
		return false;
	}
	
	public boolean isKeyDown(int key) {
		if(key < 0 || key > GLFW.GLFW_KEY_LAST) throw new IllegalArgumentException("Key not supported by GLFW");
		return keys[key] == 1;
	}
	
	public boolean isKeyPressed(int key) {
		if(key < 0 || key > GLFW.GLFW_KEY_LAST) throw new IllegalArgumentException("Key not supported by GLFW");
		return keysPressed[key] == 1;
	}
	
	public boolean isKeyReleased(int key) {
		if(key < 0 || key > GLFW.GLFW_KEY_LAST) throw new IllegalArgumentException("Key not supported by GLFW");
		return keysReleased[key] == 1;
	}
	
	public boolean isButtonDown(int button) {
		if(button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) throw new IllegalArgumentException("Button not supported by GLFW");
		return buttons[button] == 1;
	}
	
	public boolean isButtonPressed(int button) {
		if(button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) throw new IllegalArgumentException("Button not supported by GLFW");
		return buttonsPressed[button] == 1;
	}
	
	public boolean isButtonReleased(int button) {
		if(button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) throw new IllegalArgumentException("Button not supported by GLFW");
		return buttonsReleased[button] == 1;
	}
	
	public float getScrollY() {
		return input.getScrollY();
	}
	
	public float getScrollX() {
		return input.getScrollX();
	}
	
	public void lockCursor() {
		input.lockCursor();
	}
	
	public void unlock() {
		input.unlock();
	}
	
	public Vector2f getDisplVec() {
        return input.getDisplVec();
    }
}
