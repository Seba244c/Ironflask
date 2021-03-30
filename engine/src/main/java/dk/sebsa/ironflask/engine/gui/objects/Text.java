package dk.sebsa.ironflask.engine.gui.objects;

import java.util.Map;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer2d;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.text.Glyph;
import dk.sebsa.ironflask.engine.gui.text.Label;

public class Text extends GuiObject {
	public Label label;
	
	public Text(Label label) {
		this.label = label;
	}
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r) {
		draw(shader, mesh, r, label);
	}
	
	public static void draw(Shader shader, Mesh2d mesh, Rect r, Label label) {
		Renderer2d.prepare();
		Map<Character, Glyph> chars = label.font.getChars();
		float tempX = r.x;
		char[] c = label.getCharArray();
		
		for(int i = 0; i < c.length; i++) {
			Glyph glyph = chars.get(c[i]);
			
			Renderer2d.drawTextureWithTextCoords(label.font.getTexture(),new Rect(tempX, r.y, glyph.scale.x, glyph.scale.y), new Rect(glyph.position.x, glyph.position.y, glyph.size.x, glyph.size.y));

			tempX += glyph.scale.x;
		}
		mesh.render();
		Renderer2d.unprepare();
	}

	@Override
	public boolean handleEvent(Event e) {
		return false;
	}

}
