package dk.sebsa.ironflask.engine.core.layer;

import java.text.DecimalFormat;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventCatagory;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.ButtonPressedEvent;
import dk.sebsa.ironflask.engine.core.events.ButtonReleasedEvent;
import dk.sebsa.ironflask.engine.core.events.CharEvent;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.core.events.KeyReleasedEvent;
import dk.sebsa.ironflask.engine.core.events.MouseMoveEvent;
import dk.sebsa.ironflask.engine.core.events.MouseScrolledEvent;
import dk.sebsa.ironflask.engine.debug.BetterImGuiImplGlfw;
import dk.sebsa.ironflask.engine.io.Log;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.enums.*;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.utils.CommandUtils;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiInputTextFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImString;

public abstract class ImGuiLayer extends Layer {
	private Application application;
	private BetterImGuiImplGlfw imGuiImp;
	private ImGuiImplGl3 imGuiGlImp;
	private ImString consoleInput = new ImString("I NEED TO SET A MAX LENGTH SOMEHOW SO I DID IT THIS WAY. MAKE IT UNLIMTEEEEEEEEEEEEEEEEEEEED");
	
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
		
		consoleInput.clear();
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
			} else if(e.type == EventType.KeyPressed) {
				KeyPressedEvent e2 = (KeyPressedEvent) e;
				io.setKeysDown(e2.key, true);
			} else if(e.type == EventType.KeyReleased) {
				KeyReleasedEvent e2 = (KeyReleasedEvent) e;
				io.setKeysDown(e2.key, false);
			} else if(e.type == EventType.CharEvent) {
				CharEvent e2 = (CharEvent) e;
				io.addInputCharacter(e2.codePoint);
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
		
		// Engine settings
		ImGui.begin("Engine Settings");
		
		if(ImGui.checkbox("vSync", application.window.isVSync())) application.window.setVSync(!application.window.isVSync());
		if(ImGui.checkbox("Show Cursor", application.window.isCursorShown())) application.window.showCursor(!application.window.isCursorShown());
		if(ImGui.checkbox("Trace Logs", LoggingUtil.traceLog)) LoggingUtil.traceLog = !LoggingUtil.traceLog;
		
		ImGui.end();
		
		// Console
		ImGui.begin("Console");
		
		if(ImGui.inputTextWithHint("", "Console Command", consoleInput, ImGuiInputTextFlags.EnterReturnsTrue)) {
			CommandUtils.runCommad(consoleInput.get());
			LoggingUtil.coreLog(Severity.Info, "Debug layer ran command: " + consoleInput.get());
			consoleInput.clear();
		}
		
		ImGui.separator();
		
		ImGui.beginChild("Log");
		
		for(Log log : Log.logs) {
			Color textColor;
			if(log.severity == Severity.Error) 			textColor = Color.logError();
			else if(log.severity == Severity.Warning) 	textColor = Color.logWarning();
			else if(log.severity == Severity.Info) 		textColor = Color.logInfo();
			else 										textColor = Color.logTrace();
			ImGui.textColored(textColor.r, textColor.g, textColor.b, 1f, log.toString());
		}
		ImGui.endChild();
		
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
