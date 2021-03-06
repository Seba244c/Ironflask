package dk.sebsa.ironflask.engine.ecs.components;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Texture;

public class EntityRenderer extends Component {
	private Mesh mesh;
	private Texture texture;
	public static List<EntityRenderer> ers = new ArrayList<>();
	
	public EntityRenderer(Mesh mesh, Texture texture) {
		this.mesh = mesh;
		this.texture = texture;
	}
	
	public void onWillRender() {
		ers.add(this);
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
