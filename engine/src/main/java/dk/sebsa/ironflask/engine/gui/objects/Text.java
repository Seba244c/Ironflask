package dk.sebsa.ironflask.engine.gui.objects;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.renderers.GuiRenderer;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer2d;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Parent;
import dk.sebsa.ironflask.engine.gui.text.Glyph;
import dk.sebsa.ironflask.engine.gui.text.Label;

public class Text extends GuiObject {
	public Label label;
	
	public Text(Parent parent, Label label, boolean centered) {
		super(parent);
		this.label = label;
		this.centered = centered;
	}
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r, GuiRenderer renderer) {
		draw(shader, mesh, r, label, false, scale);
	}
	
	public static void draw(Shader shader, Mesh2d mesh, Rect rect, Label label, boolean centered, float scale) {
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
		shader.setUniform("useColor", 0);
		
		for(int i = 0; i < c.length; i++) {
			Glyph glyph = chars.get(c[i]);

			Renderer2d.drawTextureWithTextCoords(null, new Rect(tempX, r.y, glyph.scale.x, glyph.scale.y), new Rect(glyph.position.x, glyph.position.y, glyph.size.x, glyph.size.y), shader);
			
			tempX += glyph.scale.x*scale;
		}
		mesh.render();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	@Override
	public boolean handleEvent(Event e) {
		return false;
	}

}
