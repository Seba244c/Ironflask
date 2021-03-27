package dk.sebsa.ironflask.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SkyBox {
	public Material mat;
	public Mesh mesh;
	
	private byte dirty = 1;
	private Matrix4f modelViewMatrix;

	public Vector3f position = new Vector3f();
	public float scale = 100.0f;
	public Vector3f rotation = new Vector3f();
	
	public SkyBox(Material material) {
		mat = material;
		mesh = Mesh.getMesh("skybox.obj");
	}
	
	public Matrix4f getModelViewMatrix(Transformation transformation, Matrix4f viewMatrix, boolean camDirty) {
		if(dirty == 1 || camDirty) {
			modelViewMatrix = transformation.getModelViewMatrix(this, viewMatrix);
			dirty = 0;
		}
		return modelViewMatrix;
	}
}
