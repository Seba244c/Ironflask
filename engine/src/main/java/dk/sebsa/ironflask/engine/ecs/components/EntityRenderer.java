package dk.sebsa.ironflask.engine.ecs.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;

public class EntityRenderer extends Component {
	private Mesh mesh;
	private Shader shader;
	private Texture texture;
	public static HashMap<Shader, HashMap<Mesh, List<EntityRenderer>>> ers = new HashMap<>();
	
	public EntityRenderer(Mesh mesh, Texture texture, Shader shader) {
		this.mesh = mesh;
		this.texture = texture;
		this.shader = shader;
		
		try {
			shader.createUniform("projectionMatrix");
			shader.createUniform("worldMatrix");
			shader.createUniform("texture_sampler");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void onWillRender() {
		if(!ers.containsKey(shader)) ers.put(shader, new HashMap<>());
		if(!ers.get(shader).containsKey(ers)) ers.get(shader).put(mesh, new ArrayList<>());
		
		ers.get(shader).get(mesh).add(this);
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
