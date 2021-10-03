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

public class SpriteSheet extends Asset {
	private static List<SpriteSheet> sheets = new ArrayList<>();
	private List<Sprite> sprites = new ArrayList<>();
	private Material material;
	
	@SuppressWarnings ("resource")
	public SpriteSheet(String name) throws AssetExistsException {
		super(name);

		BufferedReader br;
		try {
			if(name.startsWith("/")) {
				InputStreamReader isr =  new InputStreamReader(Material.class.getResourceAsStream("/sheets" + name + ".isht"));
				br = new BufferedReader(isr);
				this.name = name.replaceFirst("/", "");
			} else {
				file = new File(name + ".isht");
				
				br = new BufferedReader(new FileReader(file));
				String[] split = name.replaceAll(Pattern.quote("\\"), "\\\\").split("\\\\");
				this.name = split[split.length - 1];
			}
			
			// Get material
			material = Material.getMaterial(br.readLine().split(" ")[1]);
			
			String line = br.readLine();
			while(line!=null) {
				String sprName = line.split(" ")[1];
				
				String[] o = br.readLine().split(" ")[1].split(",");
				Rect offset = new Rect(Float.parseFloat(o[0]), Float.parseFloat(o[1]), Float.parseFloat(o[2]), Float.parseFloat(o[3]));
				
				String[] p = br.readLine().split(" ")[1].split(",");
				Rect padding = new Rect(Float.parseFloat(p[0]), Float.parseFloat(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]));
				
				// Create and manage sprite
				Sprite ns = new Sprite(sprName, material, offset, padding);
				sprites.add(ns);
				
				// Read Line
				line = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sheets.add(this);
	}
	
	public Sprite getSprite(String name) {
		for(Sprite sprite : sprites) {
			if(sprite.name.equals(name)) return sprite;
		}
		
		return null;
	}
	
	public static SpriteSheet getSheet(String name) {
		for(SpriteSheet sheet : sheets) {
			if(sheet.name.equals(name)) return sheet;
		}
		
		return null;
	}
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
