package dk.sebsa.ironflask.engine.gui.objects;

import java.util.function.Consumer;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.ButtonPressedEvent;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.renderers.GuiRenderer;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Parent;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.io.Input;

public class Button extends GuiObject {
	private Rect clickRect;
	private Consumer<Button> clickConsumer;
	private Input input;
	public Label label;
	
	public Button(Parent parent, Input input, Consumer<Button> clickConsumer, Label label, boolean centered) {
		super(parent);
		this.clickConsumer = clickConsumer;
		this.label = label;
		this.centered = centered;
		this.input = input;
	}
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r, GuiRenderer renderer) {
		draw(shader, mesh, r, sprite, label, centered, scale);
		this.clickRect = r;
	}
	
	public static void draw(Shader shader, Mesh2d mesh, Rect rect, Sprite sprite, Label label, boolean centered, float scale) {
		Box.draw(shader, mesh, rect, sprite);
		Text.draw(shader, mesh, rect, label, centered, scale);
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type != EventType.MouseButtonPressed || clickRect == null) return false;
		ButtonPressedEvent event = (ButtonPressedEvent) e;
		
		if(event.button == 0 && clickRect.contains(input.getMousePos())) {
			if(clickConsumer != null) clickConsumer.accept(this);
			return true;
		}
		return false;
	}
}
