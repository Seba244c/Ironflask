package dk.sebsa.ironflask.engine.io;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.events.Event;
import dk.sebsa.ironflask.engine.core.events.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.events.Event.EventType;
import dk.sebsa.ironflask.engine.io.LoggingUtil.Severity;
import dk.sebsa.ironflask.engine.math.Color;

public class Window {
	public long windowId;
	private int width;
	private int height;
	private String title;
	
	private boolean vSync;
    private boolean isFullscreen;
    private boolean resized;
	private byte minimized;
    
    private int[] posX = new int[1];
    private int[] posY = new int[1];
    
    private Application app;
	
	@SuppressWarnings("resource")
	public Window(String title, int width, int height, boolean vsync, Color clearColor, Application app) {
		this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vsync;
        this.app = app;
        
        //-- Init --//
        LoggingUtil.coreLog(Severity.Info, "Creating window: "+title);
        
        // Setup an error callback. The default implementation
 		// will print the error message in System.err.
 		GLFWErrorCallback.createPrint(System.err).set();
 		
 		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() ) {
			LoggingUtil.coreLog(Severity.Error, "Unable to initialize GLFW");
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		
		// OSX Sipport
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		// Create the window
		windowId = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if ( windowId == NULL ) {
			LoggingUtil.coreLog(Severity.Error, "Failed to create the GLFW window");
			throw new IllegalStateException("Failed to create the GLFW window");
		}
		
		// Setup resize callback
        glfwSetFramebufferSizeCallback(windowId, (window, w, h) -> {
        	if(w == 0 && h == 0) {
        		minimized = 1;

                Event event = new Event(EventType.WindowMinimize, EventCatagory.Window);
                event.oneLayer = false;
                event.name = title + " was minimized";
                event.dispatch(app.stack);
        	}
    		else if(isMinimized()) {
    			minimized = 0;
    			
    			Event event = new Event(EventType.WindowUnMinimize, EventCatagory.Window);
    			event.oneLayer = false;
                event.name = title + " was un-minimized";
                event.dispatch(app.stack);
    		}
            this.width = width;
            this.height = height;
            this.setResized(true);
                        
            glViewport(0, 0, width, height);
            
            Event event = new Event(EventType.WindowResize, EventCatagory.Window);
            event.oneLayer = false;
            event.name = title + " was resized";
            event.dispatch(app.stack);
        });
		
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(windowId, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			posX[0] = (vidmode.width() - width) / 2;
			posY[0] = (vidmode.height() - height) / 2;
			// Center the window
			glfwSetWindowPos(windowId, posX[0], posY[0]);
		}
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(windowId);
		// Enable v-sync
		if(vSync) {
			glfwSwapInterval(1);
		} else {
			glfwSwapInterval(0);
		}
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		LoggingUtil.coreLog(Severity.Info, "Setting up OpenGL");
		GL.createCapabilities();
		
		// Culling
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		// Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// Make the window visible
		glfwShowWindow(windowId);
		glClearColor(clearColor.r, clearColor.g, clearColor.b, 0.0f);
		
		// Input mode
		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
	
	public void update() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public boolean isMinimized() {
		return minimized == 1;
	}
	
	@SuppressWarnings("resource")
	public void cleanup() {
		Event event = new Event(EventType.WindowClose, EventCatagory.Window);
		event.oneLayer = false;
        event.name = title + " is closing";
        event.dispatch(app.stack);
        
		LoggingUtil.coreLog(Severity.Info, "Destorying window");
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(windowId);
		glfwDestroyWindow(windowId);

		// Terminate GLFW and free the error callback
		glfwSetErrorCallback(null);
		glfwTerminate();
		
		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(windowId);
	}

	public void setTitle(String title) {
		LoggingUtil.coreLog(Severity.Info, "Setting title of window("+title+") to: " + title);
		glfwSetWindowTitle(windowId, title);
	}
	
	public void setClearColor(Color clearColor) {
		glClearColor(clearColor.r, clearColor.g, clearColor.b, 0.0f);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void showCursor(boolean show) {
		LoggingUtil.coreLog(Severity.Info, "Toggled cursor shown for window(" + title + ") to: " + show);
    	if(!show) {
    		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    	} else {
    		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    	}
    }
	
	public boolean isFullscreen() {
		return isFullscreen;
	}

    public void setFullscreen(boolean isFullscreen) {
    	LoggingUtil.coreLog(Severity.Info, "Changed fullscren for window(" + title + ") to: " + isFullscreen);
		this.isFullscreen = isFullscreen;
		resized = true;
		int tempH = 0;
		int tempW = 0; 
		if (isFullscreen) {
			tempW = width;
			tempH = height;
			GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwGetWindowPos(windowId, posX, posY);
			glfwSetWindowMonitor(windowId, glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(), GLFW_DONT_CARE);
			// Enable v-sync
			if(vSync) {
				glfwSwapInterval(1);
			} else {
				glfwSwapInterval(0);
			}
		} else {
			glfwSetWindowMonitor(windowId, 0, posX[0], posY[0], tempW, tempH, GLFW_DONT_CARE);
		}
	}

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }
    
    public boolean isVSync() {
    	return vSync;
    }
    
    public void setVSync(boolean vsync) {
    	vSync = vsync;
    	LoggingUtil.coreLog(Severity.Info, "Changed vSync for window(" + title + ") to: " + vsync);
    	if(vSync) {
    		glfwSwapInterval(1);
    	} else {
    		glfwSwapInterval(0);
    	}
    }
}
