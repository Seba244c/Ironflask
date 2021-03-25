package dk.sebsa.ironflask.engine.graph.renderers;

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
import dk.sebsa.ironflask.engine.ecs.CameraEntity;
import dk.sebsa.ironflask.engine.ecs.components.EntityRenderer;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Transformation;


public class Renderer3d {
	private Transformation transformation;
	private Matrix4f projectionMatrix;
	
	private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final Application app;
	
	public Renderer3d(Application app) {
		this.app = app;
		
		// Crate transformation
		transformation = new Transformation();
    }
	
	public void windowResized() {
		projectionMatrix = transformation.getProjectionMatrix(FOV, app.window.getWidth(), app.window.getHeight(), Z_NEAR, Z_FAR);
	}
	
	public void render(CameraEntity camera) {
		// Update view matrix
		boolean newViewMatrix = false;
		if(camera.camDirty == 1) newViewMatrix = true;
		Matrix4f viewMatrix = camera.getViewMatrix(transformation);
        for(Shader shader : EntityRenderer.ers.keySet()) {
        	shader.bind();
            shader.setUniform("projectionMatrix", projectionMatrix);
            shader.setUniform("texture_sampler", 0);
            
	        for(Mesh mesh : EntityRenderer.ers.get(shader).keySet()) {
	        	glBindVertexArray(mesh.getVaoId());
	        	
	            for(EntityRenderer er : EntityRenderer.ers.get(shader).get(mesh)) {
	        		// Color
	        	    shader.setUniform("colour", er.getColor());
	        	    shader.setUniform("useColour", er.isTextured() ? 0 : 1);
	        	    
	            	// Set world matrix for this item
	                shader.setUniform("modelViewMatrix", er.entity.getModelViewMatrix(transformation, viewMatrix, newViewMatrix));
	                renderEntity(er);
	            }
	            
	            // Restore state
	            glBindVertexArray(0);
	        }
	        
	        shader.unbind();
        }
        EntityRenderer.ers.clear();
    }
	
	public void renderEntity(EntityRenderer er) {
		// Texute
		if (er.isTextured()) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, er.getTexture().getId());
		} else { glBindTexture(GL_TEXTURE_2D, 0); }
		
		// Draw the mesh
        glDrawElements(GL_TRIANGLES, er.getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);        
	}
}
