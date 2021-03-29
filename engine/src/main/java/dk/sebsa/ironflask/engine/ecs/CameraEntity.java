package dk.sebsa.ironflask.engine.ecs;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import dk.sebsa.ironflask.engine.graph.Transformation;

public class CameraEntity extends Entity {
	public CameraEntity(boolean addToMaster) {
		super(addToMaster);
	}

	private byte camDirty = 1;
	private boolean wasDirty;
	
	private Matrix4f viewMatrix;
	
	@Override
    public void movePosition(Vector3f v) { movePosition(v.x, v.y, v.z); }
	@Override
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
        
        if(offsetX != 0 || offsetY != 0 || offsetZ != 0) {
            dirt();
        	dirty = 1;
        }
    }
    
    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
        dirt();
    }
	
	public Matrix4f getViewMatrix(Transformation transformation) {
		if(camDirty == 1) {
			viewMatrix = transformation.getViewMatrix(this);
			camDirty = 0;
		}
		return viewMatrix;
	}
	
	public boolean wasDirty() { return wasDirty; }
	
	public void lateUpdate() {
		wasDirty = false;
	}
	
	private void dirt() {
		camDirty = 1;
        wasDirty = true;
	}
	
	// TRANSFORM OVVERIDE
	@Override
	public void setLocalPosition(Vector3f position) { super.setLocalPosition(position); dirt(); }
	@Override
	public void setLocalScale(float scale) {super.setLocalScale(scale); dirt(); }
	@Override
	public void setLocalRotation(Vector3f rotation) { super.setLocalRotation(rotation); camDirty = 1; dirt();}
}