package dk.sebsa.ironflask.engine.core.layer;

import java.text.DecimalFormat;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.ButtonPressedEvent;
import dk.sebsa.ironflask.engine.core.events.ButtonReleasedEvent;
import dk.sebsa.ironflask.engine.core.events.MouseMoveEvent;
import dk.sebsa.ironflask.engine.core.events.MouseScrolledEvent;
import dk.sebsa.ironflask.engine.debug.BetterImGuiImplGlfw;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;

public abstract class ImGuiLayer extends Layer {
	private Application application;
	private BetterImGuiImplGlfw imGuiImp;
	private ImGuiImplGl3 imGuiGlImp;
	
	public ImGuiLayer(Application app) {
		super();
		this.application = app;
		
		ImGui.createContext();
		imGuiImp = new BetterImGuiImplGlfw();
		imGuiImp.init(app.window.windowId, false);
		imGuiGlImp = new ImGuiImplGl3();
		imGuiGlImp.init("#version 150");
        ImGui.init();
		ImGui.styleColorsDark();
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.catagory.equals(EventCatagory.Input)) {
			final ImGuiIO io = ImGui.getIO();
			if(e.type == EventType.MouseMoved) {
				MouseMoveEvent e2 = (MouseMoveEvent) e;
				io.setMousePos((float) e2.mousePosX[0], (float) e2.mousePosY[0]);
			} else if(e.type == EventType.MouseButtonPressed) {
				ButtonPressedEvent e2 = (ButtonPressedEvent) e;
				io.setMouseDown(e2.button, true);
			} else if(e.type == EventType.MouseButtonReleased) {
				ButtonReleasedEvent e2 = (ButtonReleasedEvent) e;
				io.setMouseDown(e2.button, false);
			} else if(e.type == EventType.MouseScrolled) {
				MouseScrolledEvent e2 = (MouseScrolledEvent) e;
				io.setMouseWheelH(io.getMouseWheelH() + (float) e2.ofsetX);
		        io.setMouseWheel(io.getMouseWheel() + (float) e2.ofsetY);
			}
		}
		return false;
	}

	@Override
	public void close() {
		imGuiImp.dispose();
		imGuiGlImp.dispose();
		ImGui.destroyContext();
	}
	
	public void draw() {
		// aft
		DecimalFormat df = new DecimalFormat("#.#####");
		String aft = df.format(application.window.getAft());
		
		ImGui.begin("Eninge Stats");
		
		// Window stats
		ImGui.text("Window Stats:");
		ImGui.text("FPS: " + application.window.getFps());
		ImGui.text("AFT: " + aft);
		
		// memory,
		double maxMem = Runtime.getRuntime().maxMemory() / (double)(1024 * 1024);
		double freMem = Runtime.getRuntime().freeMemory() / (double)(1024 * 1024);
		double usedMem = maxMem-freMem;
		ImGui.newLine();
		ImGui.text("Memory Stats:");
		ImGui.text("Memory Allocated: " + Math.round(maxMem) + "MB");
		ImGui.text("Memory Used: " + Math.round(usedMem) + "/" + Math.round(maxMem) + "MB");
		ImGui.text("Memory Free: " + Math.round(freMem) + "MB");
		
		ImGui.end();
		
		// Window settings
		ImGui.begin("Window Settings");
		
		if(ImGui.checkbox("vSync", application.window.isVSync())) application.window.setVSync(!application.window.isVSync());
		if(ImGui.checkbox("Show Cursor", application.window.isCursorShown())) application.window.showCursor(!application.window.isCursorShown());
		
		ImGui.end();
		
		drawCustom();
	}
	
	public abstract void drawCustom();

	@Override
	public void render() {
		// Start frame
		imGuiImp.newFrame();
        ImGui.newFrame();
        
        draw();
		
		// render
		ImGui.render();
		imGuiGlImp.renderDrawData(ImGui.getDrawData());
	}
}
