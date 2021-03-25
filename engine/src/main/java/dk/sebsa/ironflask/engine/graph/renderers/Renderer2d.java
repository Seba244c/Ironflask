package dk.sebsa.ironflask.engine.graph.renderers;

import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.io.Window;
import dk.sebsa.ironflask.engine.math.Matrix4x4;
import dk.sebsa.ironflask.engine.math.Vector2f;
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;

import static org.lwjgl.opengl.GL11.*;

public class Renderer2d {
	private static Mesh2d guiMesh;
	private static Matrix4x4 ortho;
	private static Window window;
	public static Shader shader;
	
	public static void init(Window win, Shader s) {
		float[] square = new float[] {
				0, 1, 1, 1, 1, 0,
				1, 0, 0, 0, 0, 1
		};
		window = win;
		shader = s;
		
		try {
			guiMesh = new Mesh2d("Render2d guiMesh", square, square);
		} catch (AssetExistsException e1) { e1.printStackTrace(); }
		try {
			shader.createUniform("projection");
			shader.createUniform("offset");
			shader.createUniform("pixelScale");
			shader.createUniform("screenPos");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void cleanup() {
		guiMesh.cleanup();
	}
	
	public static void prepare() {
		// Disable 3d
		glDisable(GL_DEPTH_TEST);
		
		// Render preparation
		shader.bind();
		ortho = Matrix4x4.ortho(0, window.getWidth(), window.getHeight(), 0, -1, 1);
		shader.setUniform("projection", ortho);
		guiMesh.bind();
	}
	
	public static void unprepare() {
		// Enable 3d
		glEnable(GL_DEPTH_TEST);
		
		shader.unbind();
		guiMesh.unbind();
	}
	
	public static void drawTextureWithTextCoords(Texture tex, Rect drawRect, Rect uvRect) {
		Rect r = new Rect(drawRect.x, drawRect.y, drawRect.width, drawRect.height);
		
		// uvreact
		float x = uvRect.x + (((r.x - drawRect.x) / drawRect.width) * uvRect.width);
		float y = uvRect.y + (((r.y - drawRect.y) / drawRect.height) * uvRect.height);
		Rect u = new Rect(x, y, (r.width / drawRect.width) * uvRect.width, (r.height / drawRect.height) * uvRect.height);
		
		// Draw
		tex.bind();
		
		shader.setUniform("offset", u.x, u.y, u.width, u.height);
		shader.setUniform("pixelScale", new Vector2f(r.width, r.height));
		shader.setUniform("screenPos", new Vector2f(r.x, r.y));
		
		guiMesh.render();
	}
}
