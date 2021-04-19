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
import dk.sebsa.ironflask.engine.gui.Parent;
import dk.sebsa.ironflask.engine.gui.Window;
import dk.sebsa.ironflask.engine.gui.objects.Box;
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
		if(prepare == 0) {
			LoggingUtil.coreLog(Severity.Error, "GuiRenderer, someone tried to render a window whilst GuiRenderer was unprepared");
			throw new IllegalStateException("GuiRenderer, someone tried to render a window whilst GuiRenderer was unprepared");
		}
		
		if(window.borderless) {
			renderWindowBorderless(window);
			return;
		}

		Box.draw(shader, guiMesh, window.renderRect, window.style);

		Renderer2d.setBounds(window.textRect);
		Text.draw(shader, guiMesh, window.textRect, window.label, false, 1);
		
		Renderer2d.setBounds(window.rect);
		for(GuiObject object : window.getGuiObjects()) {
			renderObject(object, window);
		}
		Renderer2d.setBounds(null);
	}
	
	private void renderWindowBorderless(Window window) {
		shader.setUniform("useColor", 1);
		shader.setUniform("pixelScale", new Vector2f(window.renderRect.width, window.renderRect.height));
		shader.setUniform("screenPos", new Vector2f(window.renderRect.x, window.renderRect.y));
		shader.setUniformAlt("backgroundColor", window.getBackgroundColor());
		guiMesh.render();
		
		Rect r = window.rect;
		window.rect = window.renderRect;
		
		Renderer2d.setBounds(window.rect);
		for(GuiObject object : window.getGuiObjects()) {
			renderObject(object, window);
		}
		window.rect = r;
		Renderer2d.setBounds(null);
	}
	
	public void renderObject(GuiObject object, Parent window) {
		object.update();
		
		Rect r = new Rect(object.rect.x + window.getRect().x, object.rect.y + window.getRect().y, object.rect.width, object.rect.height);
		
		r.width = Mathf.clamp(r.width, 0, window.getRect().width);
		r.height = Mathf.clamp(r.height, 0, window.getRect().height);
				
		if(object.sprite != null) shader.setUniform("useColor", object.sprite.material.isTextured() ? 0 : 1);
		
		for(Modifier modifier : object.modifiers) {
			modifier.apply(shader);
		}

		object.draw(shader, guiMesh, r, this);
		
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
