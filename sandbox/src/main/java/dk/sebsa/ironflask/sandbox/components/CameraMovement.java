package dk.sebsa.ironflask.sandbox.components;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import dk.sebsa.ironflask.engine.ecs.CameraEntity;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.graph.Transformation;
import dk.sebsa.ironflask.engine.math.Time;

public class CameraMovement extends Component {
	private static final float CAM_SPEED = 10f;
	private static final float MOUSE_SENSITIVITY = 0.2f;
	
	@Override
	public void update() {
		// Update rotation
		if (Component.assingedInput.isButtonPressed(1)) {
			Component.assingedInput.lockCursor();
			Component.assingedInput.window.showCursor(false);
		} if(Component.assingedInput.isButtonReleased(1)){
			Component.assingedInput.unlock();
			Component.assingedInput.window.showCursor(true);
		} if(Component.assingedInput.isButtonDown(1)) {
			((CameraEntity) entity).moveRotation(Component.assingedInput.getDisplVec().x * MOUSE_SENSITIVITY, Component.assingedInput.getDisplVec().y * MOUSE_SENSITIVITY, 0);
			if(Component.assingedInput.getDisplVec().x != 0 || Component.assingedInput.getDisplVec().y != 0) {
				((CameraEntity) entity).camDirty = 1;
			}
		}
	        
		// World pos
		Vector3f cameraInc = new Vector3f();
		cameraInc.set(0, 0, 0);
	    if (Component.assingedInput.isKeyDown(GLFW_KEY_W)) {
	        cameraInc.z = -1;
	        ((CameraEntity) entity).camDirty = 1;
	    } if (Component.assingedInput.isKeyDown(GLFW_KEY_S)) {
	        cameraInc.z += 1;
	        ((CameraEntity) entity).camDirty = 1;
	    } if (Component.assingedInput.isKeyDown(GLFW_KEY_A)) {
	        cameraInc.x = -1;
	        ((CameraEntity) entity).camDirty = 1;
	    } if (Component.assingedInput.isKeyDown(GLFW_KEY_D)) {
	        cameraInc.x += 1;
	        ((CameraEntity) entity).camDirty = 1;
	    } if (Component.assingedInput.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
	        cameraInc.y = -1;
	        ((CameraEntity) entity).camDirty = 1;
	    } if (Component.assingedInput.isKeyDown(GLFW_KEY_SPACE)) {
	        cameraInc.y += 1;
	        ((CameraEntity) entity).camDirty = 1;
	    }
	    ((CameraEntity) entity).movePosition(
	    		cameraInc.x * CAM_SPEED * Time.getDeltaTime(),
	            cameraInc.y * CAM_SPEED * Time.getDeltaTime(),
	            cameraInc.z * CAM_SPEED * Time.getDeltaTime());
	}
}
