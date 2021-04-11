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
import dk.sebsa.ironflask.engine.gui.Modifier;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.WindowWithTitle;
import dk.sebsa.ironflask.engine.gui.objects.Text;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.math.Mathf;
import dk.sebsa.ironflask.engine.math.Matrix4x4;
import dk.sebsa.ironflask.engine.math.Vector2f;

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
			shader.createUniform("useColor");
			shader.createUniform("offset");
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
		if(window.getClass().getSimpleName().contains("Title")) { renderWindowWithTitle(window); return; }
		
		if(prepare == 0) {
			LoggingUtil.coreLog(Severity.Error, "GuiRenderer, someone tried to render a window whilst GuiRenderer was unprepared");
			throw new IllegalStateException("GuiRenderer, someone tried to render a window whilst GuiRenderer was unprepared");
		}
		renderBackground(window);
		for(GuiObject object : window.getGuiObjects()) {
			renderObject(object, window);
		}
	}
	
	private void renderWindowWithTitle(Window window) {
		if(prepare == 0) {
			LoggingUtil.coreLog(Severity.Error, "GuiRenderer, someone tried to render a window whilst GuiRenderer was unprepared");
			throw new IllegalStateException("GuiRenderer, someone tried to render a window whilst GuiRenderer was unprepared");
		}
		WindowWithTitle win = ((WindowWithTitle) window);
		
		renderBackground(window);
		renderTextBackground(win);
		
		Text.draw(shader, guiMesh, win.textRect, win.label, false, 1);
		
		for(GuiObject object : window.getGuiObjects()) {
			renderObject(object, window);
		}
	}
	
	private void renderBackground(Window window) {
		shader.setUniform("useColor", 1);
		shader.setUniform("pixelScale", new Vector2f(window.rect.width, window.rect.height));
		shader.setUniform("screenPos", new Vector2f(window.rect.x, window.rect.y));
		shader.setUniformAlt("backgroundColor", window.getBackgroundColor());
		
		guiMesh.render();
	}
	
	private void renderTextBackground(WindowWithTitle win) {
		shader.setUniform("useColor", 1);
		shader.setUniform("pixelScale", new Vector2f(win.textRect.width+4, win.textRect.height));
		shader.setUniform("screenPos", new Vector2f(win.textRect.x-4, win.textRect.y));
		shader.setUniformAlt("backgroundColor", win.getBackgroundColor());
		
		guiMesh.render();
	}
	
	public void renderObject(GuiObject object, Window window) {
		object.update();
		
		Rect r = new Rect(object.rect.x + window.rect.x, object.rect.y + window.rect.y, object.rect.width, object.rect.height);
		
		r.width = Mathf.clamp(r.width, 0, window.rect.width);
		r.height = Mathf.clamp(r.height, 0, window.rect.height);
		
		shader.setUniform("useColor", object.material.isTextured() ? 0 : 1);
		
		for(Modifier modifier : object.modifiers) {
			modifier.apply(shader);
		}

		object.draw(shader, guiMesh, r);
		
		for(Modifier modifier : object.modifiers) {
			modifier.remove(shader);
		}
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
