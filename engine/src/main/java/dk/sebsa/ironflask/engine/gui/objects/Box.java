package dk.sebsa.ironflask.engine.gui.objects;

import org.lwjgl.opengl.GL11;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class Box extends GuiObject {
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r) {
		draw(shader, mesh, r, material);
	}
	
	public static void draw(Shader shader, Mesh2d mesh, Rect rect, Material material) {
		if(material.isTextured()) {
			// uvreact
			Rect u = new Rect(0,1,1,-1);
			
			// Draw
			material.texture.bind();
			
			shader.setUniform("offset", u.x, u.y, u.width, u.height);
			shader.setUniform("pixelScale", new Vector2f(-rect.width, -rect.height));
			shader.setUniform("screenPos", new Vector2f(rect.x, rect.y));
			shader.setUniformAlt("backgroundColor", material.getColor());
			
			mesh.render();
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			return;
		}
		
		shader.setUniform("pixelScale", new Vector2f(rect.width, rect.height));
		shader.setUniform("screenPos", new Vector2f(rect.x, rect.y));
		shader.setUniformAlt("backgroundColor", material.getColor());

		mesh.render();
	}

	@Override
	public boolean handleEvent(Event e) {
		return false;
	}
}
