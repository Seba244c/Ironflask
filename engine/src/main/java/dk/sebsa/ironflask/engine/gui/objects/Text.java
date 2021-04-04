package dk.sebsa.ironflask.engine.gui.objects;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.text.Glyph;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class Text extends GuiObject {
	public Label label;
	
	public Text(Label label, boolean centered) {
		this.label = label;
		this.centered = centered;
	}
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r) {
		draw(shader, mesh, r, label, false);
	}
	
	public static void draw(Shader shader, Mesh2d mesh, Rect rect, Label label, boolean centered) {
		Rect r = new Rect(rect.x, rect.y, rect.width, rect.height);
		
		if(centered) {
			r.x = r.x + r.width/2;
			r.x -= r.width/2;
		}
		if(r.x % 1 != 0) r.x += 0.5f;
		if(r.y % 1 != 0) r.y += 0.5f;
		
		Map<Character, Glyph> chars = label.font.getChars();
		float tempX = r.x;
		char[] c = label.getCharArray();
		
		label.font.getTexture().bind();
		
		for(int i = 0; i < c.length; i++) {
			Glyph glyph = chars.get(c[i]);
			
			// RENDER
			Rect drawRect = new Rect(tempX, r.y, glyph.scale.x, glyph.scale.y);
			Rect drawRect2 = new Rect(tempX, r.y, glyph.scale.x, glyph.scale.y);
			Rect uvRect = new Rect(glyph.position.x, glyph.position.y, glyph.size.x, glyph.size.y);
			// uvreact
			float x = uvRect.x + (((drawRect2.x - drawRect.x) / drawRect.width) * uvRect.width);
			float y = uvRect.y + (((drawRect2.y - drawRect.y) / drawRect.height) * uvRect.height);
			Rect u = new Rect(x, y, (drawRect2.width / drawRect.width) * uvRect.width, (drawRect2.height / drawRect.height) * uvRect.height);
			
			// Draw
			shader.setUniform("useColor", 0);
			shader.setUniform("offset", u.x, u.y, u.width, u.height);
			shader.setUniform("pixelScale", new Vector2f(drawRect2.width, drawRect2.height));
			shader.setUniform("screenPos", new Vector2f(drawRect2.x, drawRect2.y));
			
			Mesh2d.quad.render();
			tempX += glyph.scale.x;
		}
		mesh.render();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	@Override
	public boolean handleEvent(Event e) {
		return false;
	}

}
