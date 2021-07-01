package dk.sebsa.ironflask.editor.ui;

import dk.sebsa.ironflask.editor.plugin.PluginManager;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Layer;
import dk.sebsa.ironflask.engine.enums.Languages;
import dk.sebsa.ironflask.engine.local.LocalizationManager;

public class EditorLayer extends Layer {
	private Application app;
	
	public EditorLayer(Application app) {
		guiLayer = true;
		this.app = app;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		return false;
	}

	@Override
	public void render() {
		app.guiRenderer.prepareForRender();
		
		
		
		app.guiRenderer.endFrame();
	}

	@Override
	public void close() {
		
	}

	@Override
	public void init() {
		LocalizationManager.setLangauage(Languages.en);
		
		PluginManager.fullInit(app);
	}
}
