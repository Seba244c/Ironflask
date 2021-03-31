package dk.sebsa.ender.game.layers;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ender.game.Main;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.KeyPressedEvent;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVar;
import dk.sebsa.ironflask.engine.gui.GUIDynamicVector;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.enums.Anchor;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.gui.objects.Text;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;

public class MainMenu extends Layer {
	private Font titleFont;
	private Window entireScreen;
	private Application app;
	
	public MainMenu(Application app) {
		guiLayer = true;
		this.app = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.WindowResize) {
			entireScreen.calculateConstraints(app);
		} else if(e.type == EventType.KeyPressed) {
			KeyPressedEvent event = (KeyPressedEvent) e;
			if(event.key == GLFW.GLFW_KEY_TAB) {
				if(Main.debug.isEnabled()) Main.debug.setEnabled(false);
				else Main.debug.setEnabled(true);
				return true;
			}
		}
		
		return entireScreen.handleEvent(e);
	}

	@Override
	public void render() {
		app.guiRenderer.prepareForRender();
		app.guiRenderer.renderWindow(entireScreen);
		app.guiRenderer.endFrame();
	}

	@Override
	public void close() {
		
	}

	@Override
	public void init() {
		try {
			titleFont = new Font(new java.awt.Font("OpenSans", java.awt.Font.BOLD, 42));
		} catch (AssetExistsException e) { e.printStackTrace(); }
		
		// init windows
		entireScreen = new Window();
		entireScreen.setBackgroundColor(Color.transparent());
		
		// play button
		GUIDynamicVar size48 = new GUIDynamicVar(GUIDynamicType.Fixed, 48);
		Button playButton = new Button(app.input, this::play, new Label("Play!", titleFont), true);
		playButton.setAnchor(Anchor.BottomLeft);
		playButton.size = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Fixed, titleFont.getStringWidth("Play!")+4), size48);
		playButton.posistion = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Fixed, 0f), new GUIDynamicVar(GUIDynamicType.Dynamic, 0.4f));
		entireScreen.addGuiObject(playButton);
		
		// quit button
		Button quitButton = new Button(app.input, this::quit, new Label("Quit", titleFont), true);
		quitButton.setAnchor(Anchor.BottomLeft);
		quitButton.size = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Fixed, titleFont.getStringWidth("Quit")+4), size48);
		quitButton.posistion = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Fixed, 0f), new GUIDynamicVar(GUIDynamicType.Dynamic, 0.2f));
		entireScreen.addGuiObject(quitButton);
		
		// Title
		Text title = new Text(new Label("Project Ender", titleFont), true);
		title.setAnchor(Anchor.TopLeft);
		title.size = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Fixed, titleFont.getStringWidth("Project Ender")), size48);
		title.posistion = new GUIDynamicVector(new GUIDynamicVar(GUIDynamicType.Fixed, 0), new GUIDynamicVar(GUIDynamicType.Dynamic, 0.05f));
		entireScreen.addGuiObject(title);
		
		// Endoff
		entireScreen.calculateConstraints(app);
	}
	
	public void play(Button button) {
		Main.mainMenu(false);
	}
	
	public void quit(Button button) {
		app.close();
	}
}