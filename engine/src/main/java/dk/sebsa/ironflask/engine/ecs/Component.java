package dk.sebsa.ironflask.engine.ecs;

import dk.sebsa.ironflask.engine.io.Input;

public abstract class Component {
	public static Input assingedInput;
	
	private boolean enabled = true;
	
	public Entity entity;
	
	public void init(Entity owner) {
		if(this.entity==null)
			this.entity = owner;
		awake();
	}
	
	public void awake() {
		// When the component is intiliazed
	}
	
	public void update() {
		// Occurs on every frame before rendering
	}
	
	public void lateUpdate() {
		// Occurs after update
	}
	public void onWillRender() {
		// When ready to render
	}
	
	public final Entity getOwner() {
		return entity;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
