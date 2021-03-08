package dk.sebsa.ironflask.engine.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import dk.sebsa.ironflask.engine.graph.Transformation;

public class Entity {
	public static int i;
	
	public String tag = "Untagged";
	public String name = "New Entity";
	private String id;
	
	// Transform vv
	protected Vector3f position;
	protected float scale;
	protected Vector3f rotation;

	private List<Component> components = new ArrayList<Component>();
	private boolean enabled = true;
	private byte dirty = 1;
	private Matrix4f modelViewMatrix;

	public Entity() {
		this.id = UUID.randomUUID().toString();
		position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
	}
	
	public Entity(String name) {
		this.name = name;
		this.id = UUID.randomUUID().toString();
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
		dirty = 1;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
		dirty = 1;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
		dirty = 1;
	}

	public Matrix4f getModelViewMatrix(Transformation transformation, Matrix4f viewMatrix, boolean newViewMatrix) {
		if(dirty == 1 || newViewMatrix) {
			modelViewMatrix = transformation.getModelViewMatrix(this, viewMatrix);
			dirty = 0;
		}
		return modelViewMatrix;
	}
	
	public void setId(String newId) {
		id = newId;
	}
	
	public String getId() {
		return id;
	}
}