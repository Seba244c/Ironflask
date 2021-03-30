package dk.sebsa.ironflask.engine.graph.renderers;

import org.joml.Matrix4f;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.CameraEntity;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.SkyBox;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.graph.Transformation;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SkyboxRenderer {
	private Shader shader;
	private Application application;
	private Matrix4f projectionMatrix;
	private Transformation transformation;
	
	private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
	
	public SkyboxRenderer(Application application) {
		LoggingUtil.coreLog(Severity.Info, "Initiliazing SkyboxRenderer");
		
		this.application = application;
		shader = Shader.getShader("ironflask_skybox");
		try {
			shader.createUniform("projectionMatrix");
			shader.createUniform("modelViewMatrix");
			shader.createUniform("texture_sampler");
		} catch (Exception e) { e.printStackTrace(); }
		
		// Crate transformation
		transformation = new Transformation();
		
		windowResized();
    }
	
	public void windowResized() {
		projectionMatrix = transformation.getProjectionMatrix(FOV, application.window.getWidth(), application.window.getHeight(), Z_NEAR, Z_FAR);
	}
	
	public void render(CameraEntity camera) {
		if(WorldManager.getWorld().skyBox == null) return;
	    shader.bind();

	    shader.setUniform("texture_sampler", 0);

	    // Update projection Matrix
	    shader.setUniform("projectionMatrix", projectionMatrix);
	    SkyBox skyBox = WorldManager.getWorld().skyBox;
	    Matrix4f viewMatrix = transformation.getViewMatrix(camera);
	    
	    viewMatrix.m30(0);
	    viewMatrix.m31(0);
	    viewMatrix.m32(0);
	    shader.setUniform("modelViewMatrix", skyBox.getModelViewMatrix(transformation, viewMatrix, camera.wasDirty()));

	    render(skyBox.mat, skyBox.mesh);

	    shader.unbind();
	}
	
	private void render(Material material, Mesh mesh) {
		Texture texture = material.getTexture();
        if (texture != null) {
            // Activate firs texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        // Draw the mesh
        glBindVertexArray(mesh.getVaoId());

        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
	}
}
