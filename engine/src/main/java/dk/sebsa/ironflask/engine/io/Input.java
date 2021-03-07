package dk.sebsa.ironflask.engine.io;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.ButtonPressedEvent;
import dk.sebsa.ironflask.engine.core.events.ButtonReleasedEvent;
import dk.sebsa.ironflask.engine.core.events.CharEvent;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.core.events.KeyReleasedEvent;
import dk.sebsa.ironflask.engine.core.events.MouseMoveEvent;
import dk.sebsa.ironflask.engine.core.events.MouseScrolledEvent;
import dk.sebsa.ironflask.engine.io.LoggingUtil.Severity;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class Input {
	private byte[] keys;
	private byte[] keysPressed;
	private byte[] keysReleased;
	private byte[] buttons;
	private byte[] buttonsPressed;
	private byte[] buttonsReleased;
	
	private double mouseX;
	private double mouseY;
	private double prevMouseX;
	private double prevMouseY;
	private double lockMouseX;
	private double lockMouseY;
	private int scrollX;
	private int scrollY;
	private Vector2f displVec;
	public Window window;
	@SuppressWarnings("unused")
	private Application app;
	private GLFWKeyCallback keyboard;
	private GLFWCharCallback text;
	private GLFWCursorPosCallback mouseMove;
	private GLFWMouseButtonCallback mouseButtons;
	private GLFWScrollCallback mouseScroll;
	private GLFWCursorEnterCallback cursorEnter;
	private byte locked = 0;
	
	public Input(Application app) {
		LoggingUtil.coreLog(Severity.Info, "Creating input callbacks");
		// Bool to byte
		keys = new byte[GLFW.GLFW_KEY_LAST];
		keysPressed = new byte[GLFW.GLFW_KEY_LAST];
		keysReleased = new byte[GLFW.GLFW_KEY_LAST];
		buttons = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
		buttonsPressed = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
		buttonsReleased = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];

		this.app = app;
		this.window = app.window;
		
		displVec = new Vector2f();
		
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(key != -1) {
					keys[key] = (byte)(action != GLFW.GLFW_RELEASE?1:0);
					if(action == 1) {
						keysPressed[key] = 1;
						KeyPressedEvent e = new KeyPressedEvent(EventType.KeyPressed, EventCatagory.Input);
						e.key = key;
						e.dispatch(app.stack);
					}
					if(action == 0) {
						keysReleased[key] = 1;
						KeyReleasedEvent e = new KeyReleasedEvent(EventType.KeyReleased, EventCatagory.Input);
						e.key = key;
						e.dispatch(app.stack);
					}
				}
			}
		};
		
		text = new GLFWCharCallback() {
			@Override
			public void invoke(long window, int codepoint) {
				CharEvent e = new CharEvent(EventType.CharEvent, EventCatagory.Input);
				e.codePoint = codepoint;
				e.dispatch(app.stack);
			}
		};
		
		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				MouseMoveEvent e = new MouseMoveEvent(EventType.MouseMoved, EventCatagory.Input);
				e.mousePosX[0] = (int) xpos;
				e.mousePosY[0] = (int) ypos;
				e.dispatch(app.stack);
				mouseX = xpos;
				mouseY = ypos;
			}
		};
		
		mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (byte)(action != GLFW.GLFW_RELEASE?1:0);
				if(action == 1) {
					buttonsPressed[button] = 1;
					
					ButtonPressedEvent e = new ButtonPressedEvent(EventType.MouseButtonPressed, EventCatagory.Input);
					e.button = button;
					e.dispatch(app.stack);
				} else if(action == 0) {
					buttonsReleased[button] = 1;
					ButtonReleasedEvent e = new ButtonReleasedEvent(EventType.MouseButtonReleased, EventCatagory.Input);
					e.button = button;
					e.dispatch(app.stack);
				}
			}
		};
		
		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				MouseScrolledEvent e = new MouseScrolledEvent(EventType.MouseScrolled, EventCatagory.Input);
				e.oneLayer = false;
				e.ofsetX = (int) offsetx;
				e.ofsetY = (int) offsety;
				e.dispatch(app.stack);
				
				scrollX += offsetx;
				scrollY += offsety;
			}
		};
	}
	
	@SuppressWarnings("resource")
	public void addCallbacks() {
		LoggingUtil.coreLog(Severity.Info, "Setting input callbacks");
		// Set callbacks
		GLFW.glfwSetKeyCallback(window.windowId, keyboard);
		GLFW.glfwSetCursorPosCallback(window.windowId, mouseMove);
		GLFW.glfwSetScrollCallback(window.windowId, mouseScroll);
		GLFW.glfwSetMouseButtonCallback(window.windowId, mouseButtons);
		GLFW.glfwSetCursorEnterCallback(window.windowId, cursorEnter);
		GLFW.glfwSetCharCallback(window.windowId, text);
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
	
	public void cleanup() {
		LoggingUtil.coreLog(Severity.Info, "Freeing input callbacks");
		glfwFreeCallbacks(window.windowId);
	}

	public double getMouseX() {
		return mouseX;
	}

	public double getMouseY() {
		return mouseY;
	}
	
	public int getScrollX() {
		return scrollX;
	}

	public int getScrollY() {
		return scrollY;
	}

	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}
	
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}
	
	public void update() { 
		// Display vec
		displVec.zero();
		if (prevMouseX > 0 && prevMouseY> 0) {
            double deltax = mouseX - prevMouseX;
            double deltay = mouseY - prevMouseY;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
		
		prevMouseX = mouseX;
        prevMouseY = mouseY;
		
		if(locked == 1) {
			glfwSetCursorPos(window.windowId, lockMouseX, lockMouseY);
			prevMouseX = lockMouseX;
			prevMouseY = lockMouseY;
		}
	}
	
	public void late() {
		keysPressed = new byte[GLFW.GLFW_KEY_LAST];
		keysReleased = new byte[GLFW.GLFW_KEY_LAST];
		buttonsPressed = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
		buttonsReleased = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
		scrollY = 0;
		scrollX = 0;
	}
	
	public Vector2f getDisplVec() {
        return displVec;
    }
	
	public void lockCursor() {
		LoggingUtil.coreLog(Severity.Info, "Locked mouse posistion at: " + new Vector2f(mouseX, mouseY).toString());
		if(locked == 0) {
			locked = 1;
			lockMouseX = mouseX;
			lockMouseY = mouseY;
			displVec.zero();
		}
		
		glfwSetCursorPos(window.windowId, lockMouseX, lockMouseY);
		prevMouseX = lockMouseX;
		prevMouseY = lockMouseY;
	}
	
	public void unlock() {
		LoggingUtil.coreLog(Severity.Info, "Unlocked mouse posistion");
		locked = 0;
	}
}
