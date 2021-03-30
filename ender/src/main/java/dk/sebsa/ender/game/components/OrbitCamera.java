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
	public float rotateRateV = -45;
	public boolean rotateOnMove = true;
	
	private float rotateH = 0f;
	private float changeH = 0f;
	private float changeH2 = 0f;
	private float rotateV = 10f;
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
	public void update() {
		if(target == null) return;	
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
			
			rotateH = Mathf.wrap(rotateH, 0, 360);
			rotateV = Mathf.clamp(rotateV, 10, 30);

	        changeH2 += changeH;
        }
        
		if(!rotateOnMove) {
			entity.getParent().setLocalRotation(new Vector3f(0, -rotateH, 0.0f));
			entity.setLocalRotation(new Vector3f(rotateV+10, 0, 0.0f));
		} else {
			entity.setLocalRotation(new Vector3f(rotateV+10, -changeH2, 0.0f));
		}
		
		// Calculate h axis
		float ZdistanceFromMiddle = (float)Math.cos(Math.toRadians(rotateH))*distance;
		float xSide = (float)Math.sin(Math.toRadians(rotateH))*distance;
		// Calculate v axis
		float YupAndDown = (float)Math.sin(Math.toRadians(rotateV))*distance;
		ZdistanceFromMiddle *= ((float)Math.cos(Math.toRadians(rotateV)));
		
        entity.setLocalPosition(new Vector3f(xSide, YupAndDown+2.5f, ZdistanceFromMiddle));
        
		changeH = 0;
		changeV = 0;
	}

	public float getHorizontalRotate() {
		return rotateH;
	}
	
	public void rotate(Entity player) {
		changeH2 = 0;
		player.setLocalRotation(new Vector3f(0, -rotateH, 0.0f));
		entity.setLocalRotation(new Vector3f(rotateV+10, -changeH2, 0.0f));
	}

	public float getVerticalRotate() {
		return rotateV;
	}
}
