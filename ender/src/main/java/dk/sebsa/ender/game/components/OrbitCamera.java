package dk.sebsa.ender.game.components;

import org.joml.Vector3f;

import dk.sebsa.ender.game.layers.EnderGame;
import dk.sebsa.ironflask.engine.core.Event;
import dk.sebsa.ironflask.engine.core.Event.EventType;
import dk.sebsa.ironflask.engine.core.events.MouseMoveEvent;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.math.Mathf;
import dk.sebsa.ironflask.engine.math.Time;

public class OrbitCamera extends Component {
	public Entity target;
	
	private float distance = 5.0f;
	
	public float minDistance = 4f;
	public float maxDistance = 10f;
	public float zoomRate = 20;
	public float rotateRateH = 100;
	public float rotateRateV = -50;
	
	private float rotateH = 0f;
	private float changeH = 0f;
	private float rotateV = 0f;
	private float changeV = 0f;
	
	public OrbitCamera(Entity target) {
		super();
		this.target = target;
	}
	
	public boolean onEvent(Event event) {
		if(event.type == EventType.MouseMoved) {
			MouseMoveEvent e = (MouseMoveEvent) event;
			changeH = e.offsetPosX[0];
			changeV = e.offsetPosY[0];
			return true;
		}
		return false;
	}
	
	@Override
	public void lateUpdate() {
		if(target != null) {	
			// Zoom
			distance += -(Component.assingedInput.getScrollY() * Time.getDeltaTime()) * zoomRate * Mathf.abs(distance);
			if (distance < minDistance) {
	            distance = minDistance;
	        } else if (distance > maxDistance) {
	            distance = maxDistance;
	        } else if (Component.assingedInput.isButtonDown(1)) {
	        	// Rotate
	        	float change = changeH * Time.getDeltaTime() * rotateRateH * EnderGame.configSensitevity;
	        	rotateH += change;
	        	change = changeV * Time.getDeltaTime() * rotateRateV * EnderGame.configSensitevity;
	        	rotateV += change;
	        }
			
			rotateH = Mathf.wrap(rotateH, 0, 360);
			rotateV = Mathf.clamp(rotateV, 0, 90);

			entity.getParent().setLocalRotation(new Vector3f(0, -rotateH, 0.0f));
			entity.setLocalRotation(new Vector3f(10, 0, 0.0f));			
	        entity.setLocalPosition(new Vector3f((float)Math.sin(Math.toRadians(rotateH))*distance, 2.0f, (float)Math.cos(Math.toRadians(rotateH))*distance));
		}
		changeH = 0;
	}
}
