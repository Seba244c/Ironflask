package dk.sebsa.ironflask.sandbox.debug;

import java.text.DecimalFormat;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.events.Event;
import dk.sebsa.ironflask.engine.core.layer.Layer;
import imgui.glfw.ImGuiImplGlfw;  
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;

public class ImGuiLayer extends Layer {
	private Application application;
	private ImGuiImplGlfw imGuiImp;
	private ImGuiImplGl3 imGuiGlImp;
	
	public ImGuiLayer(Application app) {
		super();
		this.application = app;
		
		ImGui.createContext();
		imGuiImp = new ImGuiImplGlfw();
		imGuiImp.init(app.window.windowId, false);
		imGuiGlImp = new ImGuiImplGl3();
		imGuiGlImp.init("#version 150");
        ImGui.init();
		ImGui.styleColorsDark();
	}
	
	@Override
	public boolean handleEvent(Event e) {
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
	}

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
