package dk.sebsa.ironflask.engine.ecs.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Shader;

public class EntityRenderer extends Component {
	private Mesh mesh;
	private Shader shader;
	private Material material;
    private static boolean first = true;
	public static HashMap<Shader, HashMap<Mesh, List<EntityRenderer>>> ers = new HashMap<>();
	
	public EntityRenderer(Mesh mesh, Material material, Shader shader) {
		this.mesh = mesh;
		this.material = material;
		this.shader = shader;
		if(!first) return;
		first = false;
		try {
			shader.createUniform("projectionMatrix");
			shader.createUniform("modelViewMatrix");
			shader.createUniform("texture_sampler");
			shader.createUniform("colour");
			shader.createUniform("useColour");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void onWillRender() {
		if(!ers.containsKey(shader)) ers.put(shader, new HashMap<>());
		if(!ers.get(shader).containsKey(mesh)) ers.get(shader).put(mesh, new ArrayList<>());
		
		ers.get(shader).get(mesh).add(this);
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public Material getMaterial() {
		return material;
	}
}
