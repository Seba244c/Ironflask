package dk.sebsa.ironflask.engine.graph;

import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.components.EntityRenderer;


public class Renderer3d extends Renderer {
	private Transformation transformation;
	private Matrix4f projectionMatrix;
	public Shader shader;
	
	private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final Application app;
	
	public Renderer3d(Shader shader, Application app) {
		this.shader = shader;
		this.app = app;
		
		// Crate transformation
		transformation = new Transformation();
		projectionMatrix = transformation.getProjectionMatrix(FOV, app.window.getWidth(), app.window.getHeight(), Z_NEAR, Z_FAR);
		
		try {
			shader.createUniform("projectionMatrix");
			shader.createUniform("worldMatrix");
			shader.createUniform("texture_sampler");
		} catch (Exception e) { e.printStackTrace(); }
    }
	
	public void windowResized() {
		projectionMatrix = transformation.getProjectionMatrix(FOV, app.window.getWidth(), app.window.getHeight(), Z_NEAR, Z_FAR);
	}
	
	public void render() {
        shader.bind();
        
        // Update projection Matrix
        
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Draw entities
        shader.setUniform("texture_sampler", 0);
        for(EntityRenderer er : EntityRenderer.ers) {
        	// Set world matrix for this item
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    er.entity.getPosition(),
                    er.entity.getRotation(),
                    er.entity.getScale());
            shader.setUniform("worldMatrix", worldMatrix);
            renderEntity(er);
        }
        EntityRenderer.ers.clear();
        
        // Restore state
        glBindVertexArray(0);

        shader.unbind();
    }
	
	public void renderEntity(EntityRenderer er) {
		// Activate first texture unit
		glActiveTexture(GL_TEXTURE0);
		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, er.getTexture().getId());
		
		// Draw the mesh
        glBindVertexArray(er.getMesh().getVaoId());
        
        glDrawElements(GL_TRIANGLES, er.getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
}
