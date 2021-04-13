package dk.sebsa.ironflask.engine.gui.objects;

import java.util.function.Consumer;

import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.ButtonPressedEvent;
import dk.sebsa.ironflask.engine.core.events.ButtonReleasedEvent;
import dk.sebsa.ironflask.engine.core.events.MouseMoveEvent;
import dk.sebsa.ironflask.engine.graph.Mesh2d;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.gui.GuiObject;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.io.Input;
import dk.sebsa.ironflask.engine.math.Mathf;
import dk.sebsa.ironflask.engine.math.Vector2f;

public class Slider extends GuiObject {
	private Input input;
	public float value = 0.5f;
	private Sprite sliderSprite;
	private Rect slideRect;
	private float worth;
	private boolean sliding;
	private Consumer<Float> valueChangedConsumer;
	
	public Slider(Input input, Sprite sliderSprite, Consumer<Float> valueChangedConsumer) {
		this.input = input;
		this.sliderSprite = sliderSprite;
		this.valueChangedConsumer = valueChangedConsumer;
	}
	
	@Override
	public void render(Shader shader, Mesh2d mesh, Rect r) {
		worth = 1/r.width;
		slideRect = draw(shader, mesh, r, sprite, value, sliderSprite);
		slideRect.x -= 5;
		slideRect.width += 10;
		if(sliding && !slideRect.contains(new Vector2f(input.getMouseX(), input.getMouseY()))) {
			sliding = false;
		}
	}
	
	public static Rect draw(Shader shader, Mesh2d mesh, Rect rect, Sprite sprite, float sliderPos, Sprite sliderSprite) {
		Rect slideRect = new Rect(rect.x+(rect.width*sliderPos)-5, rect.y, 10, rect.height*0.8f);
		Rect boxRect = new Rect(rect.x, rect.y+(rect.height*0.3f), rect.width, rect.height*0.2f);
		Box.draw(shader, mesh, boxRect, sprite);
		Box.draw(shader, mesh, slideRect, sliderSprite);
		return slideRect;
	}
	
	@Override
	public boolean handleEvent(Event e) {
		if(e.type == EventType.MouseButtonPressed) {
			if(((ButtonPressedEvent) e).button != 0) return false;
			if(slideRect.contains(new Vector2f(input.getMouseX(), input.getMouseY()))) {
				sliding = true;
				return true;
			}
		} else if(e.type == EventType.MouseButtonReleased) {
			if(((ButtonReleasedEvent) e).button != 0) return false;
			if(sliding) {
				sliding = false;
				return true;
			}
		} else if(e.type == EventType.MouseMoved) {
			if(sliding) {
				MouseMoveEvent event = (MouseMoveEvent) e;
				value += event.offsetPosX[0] * -1 * worth;
				value = Mathf.clamp(value, 0, 1);
				valueChangedConsumer.accept(value);
				return true;
			}
		}
		return false;
	}
}
