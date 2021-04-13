package dk.sebsa.ironflask.engine.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dk.sebsa.ironflask.engine.core.Asset;
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Rect;
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;

public class Sprite extends Asset {
    private static List<Sprite> sprites = new ArrayList<>();
	public Material material;
	public Rect offset;
	public Rect padding;
	
	@SuppressWarnings("resource")
	public Sprite(String name) throws AssetExistsException {
		super(name);
		
		BufferedReader br;
		try {
			if(name.startsWith("/")) {
				System.out.println("/sprites" + name + ".ispr");
				InputStreamReader isr =  new InputStreamReader(Material.class.getResourceAsStream("/sprites" + name + ".ispr"));
				br = new BufferedReader(isr);
				this.name = name.replaceFirst("/", "");
			} else {
				file = new File(name + ".ispr");
				
				br = new BufferedReader(new FileReader(file));
				String[] split = name.replaceAll(Pattern.quote("\\"), "\\\\").split("\\\\");
				this.name = split[split.length - 1];
			}
			
			// Get material
			material = Material.getMaterial(br.readLine().split(" ")[1]);	
				
			// Get offset
			String[] o = br.readLine().split(" ")[1].split(",");
			offset = new Rect(Float.parseFloat(o[0]), Float.parseFloat(o[1]), Float.parseFloat(o[2]), Float.parseFloat(o[3]));
			
			// Get padding
			String[] p = br.readLine().split(" ")[1].split(",");
			padding = new Rect(Float.parseFloat(p[0]), Float.parseFloat(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]));

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sprites.add(this);
	}
	
	public Sprite(String name, Material material, Rect offset, Rect padding) {
		this.name = name;
		this.material = material;
		this.offset = offset;
		this.padding = padding;
	}

	public static Sprite getSprite(String name) {
		for(Sprite sprite : sprites) {
			if(sprite.name.equals(name)) return sprite;
		}
		
		return null;
	}
	
	public Rect getUV() {
		if(offset == null) return null;
		
		float w = material.texture.getWidth();
		float h = material.texture.getHeight();
		return new Rect(offset.x / w, offset.y / h, offset.width / w, offset.height / h);
	}
	
	public Rect getPaddingUV() {
		if(padding == null) return null;
		
		float w = material.texture.getWidth();
		float h = material.texture.getHeight();
		return new Rect(padding.x / w, padding.y / h, padding.width / w, padding.height / h);
	}
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
	}
}
