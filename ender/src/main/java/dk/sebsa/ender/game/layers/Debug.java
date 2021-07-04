package dk.sebsa.ender.game.layers;

import dk.sebsa.ender.game.MusicManager;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.core.GUILayer;
import dk.sebsa.ironflask.engine.debug.ImGuiLayer;
import imgui.ImGui;

@GUILayer
public class Debug extends ImGuiLayer {
	private final float[] audioLevel = new float[1];
	
	public Debug(Application app) {
		super(app);
	}

	@Override
	public void drawCustom() {
		audioLevel[0] = MusicManager.musicLevel;
		ImGui.begin("AudioManager");
		ImGui.sliderFloat("Music Level", audioLevel, 0, 1);
		ImGui.end();
		if(audioLevel[0] != MusicManager.musicLevel) MusicManager.setLevel(audioLevel[0]);
	}
}
