package dk.sebsa.ironflask.engine.graph.renderers;

import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.io.Window;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.math.Matrix4x4;
import dk.sebsa.ironflask.engine.math.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer2d {
	private static Mesh2d guiMesh;
	private static Matrix4x4 ortho;
	private static Window window;
	public static Shader defaultShader;
	
	public static void init(Window win, Shader s) {
		LoggingUtil.coreLog(Severity.Info, "Initiliazing Renderer2d");
		window = win;
		defaultShader = s;
		
		guiMesh = Mesh2d.quad;
		try {
			defaultShader.createUniform("projection");
			defaultShader.createUniform("offset");
			defaultShader.createUniform("pixelScale");
			defaultShader.createUniform("screenPos");
			defaultShader.createUniform("color");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void cleanup() {
    	LoggingUtil.coreLog(Severity.Info, "Cleanup Renderer2d");
		guiMesh.cleanup();
	}
	
	public static void prepare() {
		// Disable 3d
		glDisable(GL_DEPTH_TEST);
		
		// Render preparation
		defaultShader.bind();
		ortho = Matrix4x4.ortho(0, window.getWidth(), window.getHeight(), 0, -1, 1);
		defaultShader.setUniform("projection", ortho);
		defaultShader.setUniformAlt("color", Color.white());
		guiMesh.bind();
	}
	
	public static void unprepare() {
		// Enable 3d
		glEnable(GL_DEPTH_TEST);
		
		defaultShader.unbind();
		guiMesh.unbind();
	}
	
	public static void drawTextureWithTextCoords(Texture tex, Rect drawRect, Rect uvRect, Shader shader) {
		drawTextureWithTextCoords(tex, drawRect, uvRect, guiMesh, shader);
	}
	
	public static void drawTextureWithTextCoords(Texture tex, Rect drawRect, Rect uvRect) {
		drawTextureWithTextCoords(tex, drawRect, uvRect, guiMesh, defaultShader);
	}
	
	public static void drawTextureWithTextCoords(Texture tex, Rect drawRect, Rect uvRect, Mesh2d mesh, Shader shader) {
		Rect r = new Rect(drawRect.x, drawRect.y, drawRect.width, drawRect.height);
		
		// uvreact
		float x = uvRect.x + (((r.x - drawRect.x) / drawRect.width) * uvRect.width);
		float y = uvRect.y + (((r.y - drawRect.y) / drawRect.height) * uvRect.height);
		Rect u = new Rect(x, y, (r.width / drawRect.width) * uvRect.width, (r.height / drawRect.height) * uvRect.height);
		
		// Draw
		if(tex != null) tex.bind();
		
		shader.setUniform("offset", u.x, u.y, u.width, u.height);
		shader.setUniform("pixelScale", new Vector2f(r.width, r.height));
		shader.setUniform("screenPos", new Vector2f(r.x, r.y));
		
		guiMesh.render();
	}
}
