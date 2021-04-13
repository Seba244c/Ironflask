package dk.sebsa.ironflask.engine.local;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dk.sebsa.ironflask.engine.enums.Languages;
import dk.sebsa.ironflask.engine.utils.FileUtil;

public class Language {
	public final Languages code;
	public final Map<String, String> map;
	private final JSONParser parser = new JSONParser();
	
	public Language(String file) {
		this.map = new HashMap<>();
		this.code = init(file);
	}
	
	private Languages init(String file) {
		try {
			JSONObject json = (JSONObject) parser.parse(FileUtil.readAnyFile(file));
			
			for(Object key : json.keySet()) {
				String keyString = (String) key;
				map.put(keyString, (String) json.get(keyString));
			}
		} catch (Exception e) { e.printStackTrace(); }
		
		// Code
		String[] hello;
		if(file.contains("\\")) hello = file.split("\\\\");
		else hello = file.split("/");
		
		String world = hello[hello.length-1].split("\\.")[0];
		
		return Languages.valueOf(world);
	}
	
	public String get(String key) {
		if(!map.containsKey(key)) return key;
		
		String returnString = map.get(key);
		
		return returnString;
	}
}
