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
import org.lwjgl.opengl.GLCapabilities;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.enums.*;
import dk.sebsa.ironflask.engine.graph.Rect;

public class Window {
	public long windowId;
	private int width;
	private int height;
	private String title;
	private Rect rect;
	
	private byte vSync;
    private byte isFullscreen;
	private byte minimized;
	private byte cursorShown = 1;
	private byte lineMode = 0;
	private Color clearColor;
    
    private int[] posX = new int[1];
    private int[] posY = new int[1];
    
    private Application app;
	public GLCapabilities capabilities;
	
	private int frames;	// Frames this seconds
	private int fps;	// Last seconds FPS
	
	private double aft;
	
	// Vars used in Averege frame length calculation
	private double lastFrameTime;
	private double frameTimeThisSecond;
	private static long time;
	
	// Flags
	private static int flagResize = GLFW_TRUE;
	private static int flagDecorated = GLFW_TRUE;
	public static void setWindowCreationFlag(WindowFlag flag, int val) {
		if(flag.equals(WindowFlag.Resizable)) flagResize = val;
		else if(flag.equals(WindowFlag.Decorated)) flagDecorated = val;
	}
	
	@SuppressWarnings("resource")
	public Window(String title, int width, int height, boolean vsync, Color clearColor, Application app) {
		this.title = title;
        this.width = width;
		this.clearColor = clearColor;
        this.height = height;
        if(vsync) this.vSync = 1;
        else this.vSync = 0;
        this.app = app;
        rect = new Rect(0, 0, width, height);
        
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
		glfwWindowHint(GLFW_RESIZABLE, flagResize); // the window will be temporaraly not be resizable
		glfwWindowHint(GLFW_DECORATED, flagDecorated); // the window will be temporaraly not be resizable
		
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
            this.width = w;
            this.height = h;
        	if(w == 0 && h == 0) {
        		minimized = 1;

                if(app != null) {
                	Event event = new Event(EventType.WindowMinimize, EventCatagory.Window);
                    event.oneLayer = false;
                    event.name = title + " was minimized";
                    event.dispatch(app.stack);
                }
        	}
    		else if(isMinimized()) {
    			minimized = 0;
    			
    			if(app != null) {
        			Event event = new Event(EventType.WindowUnMinimize, EventCatagory.Window);
        			event.oneLayer = false;
                    event.name = title + " was un-minimized";
                    event.dispatch(app.stack);
    			}
    		}
                        
            glViewport(0, 0, w, h);
    		rect = new Rect(0, 0, w, h);
            app.windowResized();
    		
            if(app != null) {
                Event event = new Event(EventType.WindowResize, EventCatagory.Window);
                event.oneLayer = false;
                event.name = title + " was resized";
                event.dispatch(app.stack);
            }
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
		if(vSync == 1) {
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
		capabilities = GL.createCapabilities();
		
		// Culling
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		// Enable depth test
		glEnable(GL_DEPTH_TEST);
		
		// Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// Anti alizin
		glfwWindowHint(GLFW_STENCIL_BITS, 4);
		glfwWindowHint(GLFW_SAMPLES, 4);
		
		// Make the window visible
		glfwShowWindow(windowId);
		glClearColor(clearColor.r, clearColor.g, clearColor.b, 1.0f);
		
		// Input mode
		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		
		// FPS counter
		frames = 0;
		fps = 0;
		time = System.currentTimeMillis();
		lastFrameTime = System.currentTimeMillis();
		frameTimeThisSecond = 0;
		aft = 0;
	}
	
	public void update() {
		// Time bewteen frames
		frameTimeThisSecond = frameTimeThisSecond + (System.currentTimeMillis() - lastFrameTime);
		lastFrameTime = System.currentTimeMillis();
		
		// Fps
		if (System.currentTimeMillis() > time + 1000) {
			fps = frames;
			time = System.currentTimeMillis();
			frames = 0;
			
			aft = frameTimeThisSecond/fps/1000;
			frameTimeThisSecond = 0;
		}
		frames++;
	}
	
	public boolean isMinimized() {
		return minimized == 1;
	}
	
	@SuppressWarnings("resource")
	public void cleanup() {
		if(app != null) {
			Event event = new Event(EventType.WindowClose, EventCatagory.Window);
			event.oneLayer = false;
	        event.name = title + " is closing";
	        event.dispatch(app.stack);
		}
        
		LoggingUtil.coreLog(Severity.Info, "Destorying window");
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(windowId);
		glfwDestroyWindow(windowId);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(windowId);
	}
	
	public void setShouldClose(boolean shouldClose) {
		glfwSetWindowShouldClose(windowId, shouldClose);
	}

	public void setPosistion(int x, int y) {
		glfwSetWindowPos(windowId, x, y);
	}

	public void setTitle(String title) {
		LoggingUtil.coreLog(Severity.Info, "Setting title of window("+title+") to: " + title);
		glfwSetWindowTitle(windowId, title);
	}
	
	public void setClearColor(Color clearColor) {
		glClearColor(clearColor.r, clearColor.g, clearColor.b, 1.0f);
		this.clearColor = clearColor;
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
    		cursorShown = 0;
    		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    	} else {
    		cursorShown = 1;
    		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    	}
    }
	
	public boolean isFullscreen() {
		return isFullscreen == 1;
	}

    public void setFullscreen(boolean isFullscreen) {
    	LoggingUtil.coreLog(Severity.Info, "Changed fullscren for window(" + title + ") to: " + isFullscreen);
		int tempH = 0;
		int tempW = 0; 
		if (isFullscreen) {
			this.isFullscreen = 1;
			tempW = width;
			tempH = height;
			GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwGetWindowPos(windowId, posX, posY);
			glfwSetWindowMonitor(windowId, glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(), GLFW_DONT_CARE);
			// Enable v-sync
			if(vSync == 1) {
				glfwSwapInterval(1);
			} else {
				glfwSwapInterval(0);
			}
		} else {
			this.isFullscreen = 0;
			glfwSetWindowMonitor(windowId, 0, posX[0], posY[0], tempW, tempH, GLFW_DONT_CARE);
		}
	}
    
    public boolean isVSync() {
    	return vSync == 1;
    }
    
    public void setVSync(boolean vsync) {
    	LoggingUtil.coreLog(Severity.Info, "Changed vSync for window(" + title + ") to: " + vsync);
    	if(vsync) {
    		this.vSync = 1;
    		glfwSwapInterval(1);
    	} else {
    		this.vSync = 0;
    		glfwSwapInterval(0);
    	}
    }

	public double getAft() {
		return aft;
	}

	public void setAft(double aft) {
		this.aft = aft;
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}
	
	public boolean isCursorShown() {
		return cursorShown == 1;
	}

	public boolean getLineMode() {
		return lineMode == 1;
	}

	public void setLineMode(boolean lineMode) {
		if(lineMode) {
			this.lineMode = 1;
			glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
		} else {
			this.lineMode = 0;
			glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
		}
	}

	public Rect getRect() {
		return rect;
	}

	public Color getClearColor() {
		return clearColor;
	}
}
