package dk.sebsa.ironflask.engine.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dk.sebsa.ironflask.engine.core.Asset;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;

public class Material extends Asset {
	public Texture texture;
    private boolean isTextured;
    private Color color;
    private static List<Material> materials = new ArrayList<>();
    
    @SuppressWarnings("resource")
	public Material(String name) throws AssetExistsException {
		super(name);
		
		BufferedReader br;
		try {
			if(name.startsWith("/")) {
				System.out.println("/materiels" + name + ".imat");
				InputStreamReader isr =  new InputStreamReader(Material.class.getResourceAsStream("/materials" + name + ".imat"));
				br = new BufferedReader(isr);
				this.name = name.replaceFirst("/", "");
			} else {
				file = new File(name + ".imat");
				
				br = new BufferedReader(new FileReader(file));
				String[] split = name.replaceAll(Pattern.quote("\\"), "\\\\").split("\\\\");
				this.name = split[split.length - 1];
			}
			
			// Get color
			String[] c = br.readLine().split(" ")[1].split(",");
			color = new Color(Float.parseFloat(c[0]), Float.parseFloat(c[1]), Float.parseFloat(c[2]), Float.parseFloat(c[3]));
			
			// Get texture
			texture = Texture.getTexture(br.readLine().split(" ")[1]);
			if(texture == null) {
		    	this.isTextured = false;
			} else this.isTextured = true;

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		materials.add(this);
    }

	public Material(Texture texture) {
		this.texture = texture;
		this.color = Color.white();
		this.isTextured = true;
	}

	public Material(Color color) {
		this.color = color;
		this.isTextured = false;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		isTextured = texture != null;
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
	
	public static Material getMaterial(String name) {
		for(Material material : materials) {
			if(material.name.equals(name)) return material;
		}
		return null;
	}

	@Override
	public void cleanup() {
		// SOMETHING SO THAT ECLIPSE DOESNT WANNA KILL ME. WAIT NO NO... STOP! DONT D...
	}
}
