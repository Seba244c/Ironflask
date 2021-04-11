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
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;

public class Sprite extends Asset {
    private static List<Sprite> sprites = new ArrayList<>();
	
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
			
			

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sprites.add(this);
	}
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
	}
}
