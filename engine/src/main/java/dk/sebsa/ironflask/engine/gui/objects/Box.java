package dk.sebsa.ironflask.engine.gui.objects;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.graph.renderers.GuiRenderer;
import dk.sebsa.ironflask.engine.graph.renderers.Renderer2d;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Parent;
import dk.sebsa.ironflask.engine.gui.Sprite;

public class Box extends GuiObject {
	public Box(Parent parent) {
		super(parent);
	}

	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r, GuiRenderer renderer) {
		draw(shader, mesh, r, sprite);
	}
	
	public static void draw(Shader shader, Mesh2d mesh, Rect r, Sprite e) {	
		if(!e.material.isTextured()) {
			shader.setUniform("useColor", 1);
			
			shader.setUniform("pixelScale", r.width, r.height);
			shader.setUniform("screenPos", r.x, r.y);
			shader.setUniformAlt("backgroundColor", e.material.getColor());

			mesh.render();

			return;
		}
		shader.setUniform("useColor", 0);
		
		//Cache a short variable for the texture, just so we only have to type a character anytime we use it
		Texture t = e.material.texture;
		Rect uv = e.getUV();
		
		//Get the top left corner of the box using corresponding padding values and draw it using a texture drawing method
		Rect tl = new Rect(r.x, r.y, e.padding.x, e.padding.y);
		Rect tlu = new Rect(uv.x, uv.y, e.getPaddingUV().x, e.getPaddingUV().y);
		Renderer2d.drawTextureWithTextCoords(t, tl, tlu, shader);
		
		//Get the top right corner of the box using corresponding padding values and draw it using a texture drawing method
		Rect tr = new Rect((r.x + r.width) - e.padding.width, r.y, e.padding.width, e.padding.y);
		Rect tru = new Rect((uv.x + uv.width) - e.getPaddingUV().width, uv.y, e.getPaddingUV().width, e.getPaddingUV().y);
		Renderer2d.drawTextureWithTextCoords(t, tr, tru, shader);
		
		//Get the bottom left corner of the box using corresponding padding values and draw it using a texture drawing method
		Rect bl = new Rect(r.x, (r.y + r.height) - e.padding.height, e.padding.x, e.padding.height);
		Rect blu = new Rect(uv.x, (uv.y + uv.height) - e.getPaddingUV().height, e.getPaddingUV().x, e.getPaddingUV().height);
		Renderer2d.drawTextureWithTextCoords(t, bl, blu, shader);
		
		//Get the bottom right corner of the box using corresponding padding values and draw it using a texture drawing method
		Rect br = new Rect(tr.x, bl.y, e.padding.width, e.padding.height);
		Rect bru = new Rect(tru.x, blu.y, e.getPaddingUV().width, e.getPaddingUV().height);
		Renderer2d.drawTextureWithTextCoords(t, br, bru, shader);
		
		//Get the left side of the box using corresponding padding values and draw it using a texture drawing method
		Rect l = new Rect(r.x, r.y + e.padding.y, e.padding.x, r.height - (e.padding.y + e.padding.height));
		Rect lu = new Rect(uv.x, uv.y + e.getPaddingUV().y, e.getPaddingUV().x, uv.height - (e.getPaddingUV().y + e.getPaddingUV().height));
		Renderer2d.drawTextureWithTextCoords(t, l, lu, shader);
		
		//Get the right side of the box using corresponding padding values and draw it using a texture drawing method
		Rect ri = new Rect(tr.x, r.y + e.padding.y, e.padding.width, l.height);
		Rect ru = new Rect(tru.x, lu.y, e.getPaddingUV().width, lu.height);
		Renderer2d.drawTextureWithTextCoords(t, ri, ru, shader);
		
		//Get the top of the box using corresponding padding values and draw it using a texture drawing method
		Rect ti = new Rect(r.x + e.padding.x, r.y, r.width - (e.padding.x + e.padding.width), e.padding.y);
		Rect tu = new Rect(uv.x + e.getPaddingUV().x, uv.y, uv.width - (e.getPaddingUV().x + e.getPaddingUV().width), e.getPaddingUV().y);
		Renderer2d.drawTextureWithTextCoords(t, ti, tu, shader);
		
		//Get the bottom of the box using corresponding padding values and draw it using a texture drawing method
		Rect b = new Rect(ti.x, bl.y, ti.width, e.padding.height);
		Rect bu = new Rect(tu.x, blu.y, tu.width, e.getPaddingUV().height);
		Renderer2d.drawTextureWithTextCoords(t, b, bu, shader);
		
		//Get the center of the box using corresponding padding values and draw it using a texture drawing method
		Rect c = new Rect(ti.x, l.y, ti.width, l.height);
		Rect cu = new Rect(tu.x, lu.y, tu.width, lu.height);
		Renderer2d.drawTextureWithTextCoords(t, c, cu, shader);
	}

	@Override
	public boolean handleEvent(Event e) {
		return false;
	}
}
