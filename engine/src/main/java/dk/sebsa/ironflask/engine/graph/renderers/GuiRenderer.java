package dk.sebsa.ironflask.engine.graph.renderers;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.math.Mathf;
import dk.sebsa.ironflask.engine.math.Matrix4x4;
import dk.sebsa.ironflask.engine.math.Vector2f;
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;

public class GuiRenderer {
	private Mesh2d guiMesh;
	private Matrix4x4 ortho;
	private Application app;
	private Shader shader;
	private byte prepare = 0;
	
	public GuiRenderer(Application app) {
		this.app = app;
		
		guiMesh = Mesh2d.quad;
		
		// Shader
		shader = Shader.getShader("ironflask_gui");
		try {
			shader.createUniform("projection");
			shader.createUniform("pixelScale");
			shader.createUniform("screenPos");
			shader.createUniform("backgroundColor");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void prepareForRender() {
		prepare = 1;
		
		// Disable depth test
		glDisable(GL_DEPTH_TEST);
		
		shader.bind();
		guiMesh.bind();
		
		ortho = Matrix4x4.ortho(0, app.window.getWidth(), app.window.getHeight(), 0, -1, 1);
		shader.setUniform("projection", ortho);
	}
	
	public void renderWindow(Window window) {
		if(prepare == 0) {
			LoggingUtil.coreLog(Severity.Error, "GuiRenderer, someone tried to render a window whilst GuiRenderer was unprepared");
			throw new IllegalStateException("GuiRenderer, someone tried to render a window whilst GuiRenderer was unprepared");
		}
		renderBackground(window);
		for(GuiObject object : window.getGuiObjects()) {
			renderObject(object, window);
		}
	}
	
	public void renderBackground(Window window) {
		shader.setUniform("pixelScale", new Vector2f(window.rect.width, window.rect.height));
		shader.setUniform("screenPos", new Vector2f(window.rect.x, window.rect.y));
		shader.setUniformAlt("backgroundColor", window.getBackgroundColor());
		
		guiMesh.render();
	}
	
	public void renderObject(GuiObject object, Window window) {
		Rect rect = object.rect.add(window.rect.x, window.rect.y, 0, 0);
		rect.width = Mathf.clamp(rect.width, 0, window.rect.width);
		rect.height = Mathf.clamp(rect.height, 0, window.rect.height);
		
		shader.setUniform("pixelScale", new Vector2f(rect.width, rect.height));
		shader.setUniform("screenPos", new Vector2f(rect.x, rect.y));
		shader.setUniformAlt("backgroundColor", object.getBackgroundColor());
		guiMesh.render();
	}
	
	public void endFrame() {
		prepare = 0;
		shader.unbind();
		guiMesh.unbind();
		
		//Enable depth test
		glEnable(GL_DEPTH_TEST);
	}
	
	public void cleanup() {
		guiMesh.cleanup();
	}
}
