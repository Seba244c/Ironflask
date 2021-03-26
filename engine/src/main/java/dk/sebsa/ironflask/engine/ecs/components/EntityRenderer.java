package dk.sebsa.ironflask.engine.ecs.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.math.Color;

public class EntityRenderer extends Component {
	private Mesh mesh;
	private Shader shader;
	private Texture texture;
    private Color color;
    private static Color defaultColor = Color.marine();
    private boolean isTextured;
    private static boolean first = true;
	public static HashMap<Shader, HashMap<Mesh, List<EntityRenderer>>> ers = new HashMap<>();
	
	public EntityRenderer(Mesh mesh, Texture texture, Shader shader) {
		this.mesh = mesh;
		this.texture = texture;
		isTextured = true;
		this.shader = shader;
		this.color = defaultColor;
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
	
	public EntityRenderer(Mesh mesh, Shader shader) {
		this.mesh = mesh;
		this.shader = shader;
		isTextured = false;
		
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

	public Texture getTexture() {
		isTextured = texture != null;
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isTextured() {
		return isTextured;
	}
}
