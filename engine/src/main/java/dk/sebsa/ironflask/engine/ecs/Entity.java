package dk.sebsa.ironflask.engine.ecs;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class Entity {
	public static int i;
	
	public String tag = "Untagged";
	public String name = "New Entity";
	
	// Transform vv
	private Vector3f position;
	private float scale;
	private Vector3f rotation;

	private List<Component> components = new ArrayList<Component>();
	private boolean enabled = true;

	public Entity() {
		position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
	}
	
	public Entity(String name) {
		this.name = name;
		position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
	}

	public void setEnabled(boolean e) {
		this.enabled = e;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public void removeComponent(Component c) {
		components.remove(c);
	}
	
	public Component addComponent(Component c) {
		components.add(c);
		c.init(this);
		return c;
	}

	public List<Component> getComponents() {
		return components;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
}