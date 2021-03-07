package dk.sebsa.ironflask.engine.ecs;

import org.joml.Matrix4f;

import dk.sebsa.ironflask.engine.graph.Transformation;

public class CameraEntity extends Entity {
	public byte camDirty = 1;
	private Matrix4f viewMatrix;
	
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }
    
    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }
	
	public Matrix4f getViewMatrix(Transformation transformation) {
		if(camDirty == 1) {
			viewMatrix = transformation.getViewMatrix(this);
			camDirty = 0;
		}
		return viewMatrix;
	}
}