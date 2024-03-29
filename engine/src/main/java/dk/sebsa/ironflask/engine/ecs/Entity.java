package dk.sebsa.ironflask.engine.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.graph.Transformation;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.math.Mathf;

public class Entity {
	public static int i;
	
	public String tag = "Untagged";
	public String name = "New Entity";
	private boolean doNotDelete;
	private String id;
	
	// Transform vv
	protected Vector3f position = new Vector3f();
	protected float scale = 1;
	protected Vector3f rotation = new Vector3f();
	protected Vector3f localPosition = new Vector3f();
	protected float localScale = 1;
	protected Vector3f localRotation = new Vector3f(0,0,0);
	// Transform ^^

	private List<Component> components = new ArrayList<Component>();
	private List<Entity> children = new ArrayList<Entity>();
	private Entity parent;
	private boolean enabled = true;
	protected byte dirty = 1;
	private Matrix4f modelViewMatrix;

	public Entity(boolean addToMaster) {
		this.id = UUID.randomUUID().toString();
    	LoggingUtil.coreLog(Severity.Trace, "New entity entity "+name+"("+id+")");
    	if(addToMaster) parent(WorldManager.getWorld().master);
	}
	
	public Entity(World world) {
		this.id = UUID.randomUUID().toString();
    	LoggingUtil.coreLog(Severity.Trace, "New entity entity "+name+"("+id+")");
    	parent(world.master);
	}
	
	public Entity(String name) {
		this.name = name;
		this.id = UUID.randomUUID().toString();
    	LoggingUtil.coreLog(Severity.Trace, "New entity entity "+name+"("+id+")");
    	parent(WorldManager.getWorld().master);
	}

	public void setEnabled(boolean e) {
		this.enabled = e;
	}
	
	public void delete() {
		parent.removeChild(this);
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
	
	public Entity getParent() {
		if(parent == WorldManager.getWorld().master) return null;
		return parent;
	}

	public List<Entity> getChildren() {
		return children;
	}
	
	public void parent(Entity e) {
		if(e == null) e = WorldManager.getWorld().master;
		if(parent != null) {
			if(parent != e) parent.removeChild(this);
			else return;
		}
		parent = e;
		parent.children.add(this);
		recalculateLocalTransformation();
	}
	
	public void removeChild(Entity e) {
		for(i = 0; i < children.size(); i++) {
			if(children.get(i)==e) {
				removeChild(i);
				return;
			}
		}
	}
	
	public void removeChild(int v) {
		if(v >= children.size()) return;
		
		children.remove(v);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void movePosition(Vector3f v) { movePosition(v.x, v.y, v.z); }
	public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
        	localPosition.x += (float)Math.sin(Math.toRadians(localRotation.y)) * -1.0f * offsetZ;
            localPosition.z += (float)Math.cos(Math.toRadians(localRotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
        	localPosition.x += (float)Math.sin(Math.toRadians(localRotation.y - 90)) * -1.0f * offsetX;
            localPosition.z += (float)Math.cos(Math.toRadians(localRotation.y - 90)) * offsetX;
        }
        localPosition.y += offsetY;
        
        if(offsetX != 0 || offsetY != 0 || offsetZ != 0) {
        	dirty = 1;
        }
    }
	
	// TRANSFORM
		public Vector3f getPosition() { return position; }
//		public void setPosition(Vector3f position) {this.position = position; dirty = 1; recalculateLocalTransformation(); }
		public Vector3f getLocalPosition() { return localPosition; }
		public void setLocalPosition(Vector3f position) {this.localPosition = position; dirty = 1; recalculateGlobalTransformations(); }
		
		public float getScale() { return scale; }
//		public void setScale(float scale) { this.scale = scale; dirty = 1; recalculateLocalTransformation();}
		public float getLocalScale() { return localScale; }
		public void setLocalScale(float scale) { this.scale = scale; dirty = 1; recalculateGlobalTransformations();}
	
		public Vector3f getRotation() { return rotation; }
//		public void setRotation(Vector3f rotation) { this.rotation = rotation; dirty = 1; recalculateLocalTransformation(); }
		public Vector3f getLocalRotation() { return localRotation; }
		public void setLocalRotation(Vector3f rotation) { this.localRotation = rotation; dirty = 1; recalculateGlobalTransformations(); }
		public void recalculateLocalTransformation() {
			if(this.equals(WorldManager.getWorld().master)) {
				for(int i = 0; i < children.size(); i++) children.get(i).recalculateGlobalTransformations();
				return;
			}
			dirty = 1;
			localScale = scale / parent.scale;
			localRotation = new Vector3f(
					Mathf.wrap(rotation.x - parent.rotation.x, 0, 360),
					Mathf.wrap(rotation.y - parent.rotation.y, 0, 360),
					Mathf.wrap(rotation.z - parent.rotation.z, 0, 360));
			
			for(int i = 0; i < children.size(); i++) children.get(i).recalculateGlobalTransformations();
		}
		
		private void recalculateGlobalTransformations() {
			if(this.equals(WorldManager.getWorld().master)) {
				for(int i = 0; i < children.size(); i++) children.get(i).recalculateGlobalTransformations();
				return;
			}
			dirty = 1;
			scale = parent.scale * scale;
			rotation = new Vector3f(
					Mathf.wrap(localRotation.x + parent.rotation.x, 0, 360),
					Mathf.wrap(localRotation.y + parent.rotation.y, 0, 360),
					Mathf.wrap(localRotation.z + parent.rotation.z, 0, 360));
			position = new Vector3f(parent.position.x + localPosition.x, parent.position.y + localPosition.y, parent.position.z + localPosition.z); 
			
			for(int i = 0; i < children.size(); i++) children.get(i).recalculateGlobalTransformations();
		}
		public static void recalculate() {for(int i = 0; i < WorldManager.getWorld().master.children.size(); i++) WorldManager.getWorld().master.children.get(i).recalculateGlobalTransformations();}
	// TRANSFORM END

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

	public boolean isDeletable() {
		return !doNotDelete;
	}

	public void doNotDelete() {
		this.doNotDelete = true;
	}
	
	@Override
	public String toString() {
		if(name != "New Entity") return "Entity(" + name + ")";
		else return "Entity(" + id + ")";
	}
}