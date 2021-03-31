package dk.sebsa.ironflask.engine.graph;

import dk.sebsa.ironflask.engine.math.Color;

public class Material {
	public Texture texture;
    private boolean isTextured;
    private Color color;
    private static Color defaultColor = Color.red();
    
    public Material() {
    	this.isTextured = false;
    	this.color = defaultColor;
	}
    
    public Material(Color color) {
    	this.isTextured = false;
    	this.color = color;
	}
    
    public Material(Texture texture) {
    	this.isTextured = true;
    	this.texture = texture;
    	this.color = defaultColor;
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
}
