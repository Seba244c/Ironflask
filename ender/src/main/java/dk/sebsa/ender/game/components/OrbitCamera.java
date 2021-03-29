package dk.sebsa.ender.game.components;

import org.joml.Vector3f;

import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.math.Mathf;
import dk.sebsa.ironflask.engine.math.Time;

public class OrbitCamera extends Component {
	public Entity target;
	
	private float zoomRate = 20;
	private float distance = 10.0f;

	public OrbitCamera(Entity target) {
		super();
		this.target = target;
	}
	
	@Override
	public void lateUpdate() {
		if(target != null) {	
			// Zoom
			distance += -(Component.assingedInput.getScrollY() * Time.getDeltaTime()) * zoomRate * Mathf.abs(distance);
			if (distance < 2.5f) {
	            distance = 2.5f;
	        } else if (distance > 20) {
	            distance = 20;
	        }
			
			entity.setLocalRotation(new Vector3f(0, 0, 0));
	        entity.setLocalPosition(new Vector3f(0.0f, 2.0f, distance));
		}
	}
}
