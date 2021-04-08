package dk.sebsa.ironflask.engine.utils;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BuildUtil {
	public static String id = "Cant find build.json";
	private static JSONParser jsonParser = new JSONParser();
	
	@SuppressWarnings("resource")
	public static void init() {
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("../build.json"));
			id = (String) jsonObject.get("id");
		} catch (IOException | ParseException e) {
			return;
		}
	}
}
