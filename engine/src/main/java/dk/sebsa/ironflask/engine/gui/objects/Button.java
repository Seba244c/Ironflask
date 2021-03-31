package dk.sebsa.ironflask.engine.gui.objects;

import java.util.function.Consumer;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.ButtonPressedEvent;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class Button extends GuiObject {
	private Rect clickRect;
	private Input input;
	private Consumer<Button> clickConsumer;
	public Label label;
	
	public Button(Input input, Consumer<Button> clickConsumer, Label label, boolean centered) {
		this.input = input;
		this.clickConsumer = clickConsumer;
		this.label = label;
		this.centered = centered;
	}
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r) {
		draw(shader, mesh, r, material, label, centered);
		this.clickRect = r;
	}
	
	public static void draw(Shader shader, Mesh2d mesh, Rect rect, Material material, Label label, boolean centered) {
		Box.draw(shader, mesh, rect, material);
		Text.draw(shader, mesh, rect, label, centered);
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type != EventType.MouseButtonPressed) return false;
		ButtonPressedEvent event = (ButtonPressedEvent) e;
		if(event.button == 0 && clickRect.contains(new Vector2f(input.getMouseX(), input.getMouseY()))) {
			clickConsumer.accept(this);
			return true;
		}
		return false;
	}
}
