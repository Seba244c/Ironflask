package dk.sebsa.ironflask.engine.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.graph.Mesh;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;
import dk.sebsa.ironflask.engine.gui.Sprite;
import dk.sebsa.ironflask.engine.gui.SpriteSheet;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.local.LocalizationManager;
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;
import dk.sebsa.ironflask.engine.audio.AudioClip;
import dk.sebsa.ironflask.engine.enums.*;

public class AssetManager {
	public static List<Asset> allAssets = new ArrayList<>();
	private static Class<AssetManager> clazz = AssetManager.class;
	private static ClassLoader cl = clazz.getClassLoader();
	
	private static HashMap<AssetTypes, List<String>> fileLists = new HashMap<>();
	
	public static void cleanup() {
		for(Asset a : allAssets) {
			LoggingUtil.coreLog(Severity.Trace, "Deleteing asset: " + a.name);
			a.cleanup();
		}
	}
	
	public static void loadAllResources(String externalDir) throws IOException {
		LoggingUtil.coreLog(Severity.Info, "Loading all resources");
		for(AssetTypes type : AssetTypes.values()) {
			fileLists.put(type, new ArrayList<String>());
		}
		
		initResourcePaths(externalDir);
		
		try {
			for(AssetTypes type : AssetTypes.values()) {
		    	LoggingUtil.coreLog(Severity.Info, "Creating asset list: "+type);
				createList(type);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static boolean exists(String name) {
		for(Asset a : allAssets) {
			if(a.name.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	private static void createList(AssetTypes type) throws Exception {
		List<String> list = fileLists.get(type);
		
		for(String name : list) {
			try {
				if(type.equals(AssetTypes.Texture)) new Texture(name);
				else if(type.equals(AssetTypes.Shader)) new Shader(name);
				else if(type.equals(AssetTypes.Mesh)) new Mesh(name);
				else if(type.equals(AssetTypes.AudioClip)) new AudioClip(name);
				else if(type.equals(AssetTypes.Material)) new Material(name);
				else if(type.equals(AssetTypes.Sprite)) new Sprite(name);
				else if(type.equals(AssetTypes.SpriteSheet)) new SpriteSheet(name);
				else if(type.equals(AssetTypes.Language)) LocalizationManager.load(name);
			} catch (AssetExistsException e) {
				LoggingUtil.coreLog(Severity.Warning, "Asset already exists: " + name);
			}
		}
	}
	
	private static void initResourcePaths(String externalDir) {
    	LoggingUtil.coreLog(Severity.Trace, "Init resource paths");
		URL dirUrl = cl.getResource("dk/sebsa/ironflask/engine");
		String protocol = dirUrl.getProtocol();
		
		try {
			if(dirUrl != null && protocol.equals("file")) importFromDir(externalDir);
			else importFromJar(externalDir);
		} catch (IOException e) { LoggingUtil.coreLog(Severity.Error, "Error loading assets:"); e.printStackTrace(); }
	}
	
	private static void importFromJar(String externalDir) throws UnsupportedEncodingException, IOException {
    	LoggingUtil.coreLog(Severity.Trace, "Import from jar");
		// Loads the engine resources from a jar
		String me = clazz.getName().replace(".", "/") + ".class";
		URL dirUrl = cl.getResource(me);
		
		if(dirUrl.getProtocol().equals("jar")) {
			String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entires = jar.entries();
			
			while(entires.hasMoreElements()) {
				String name = entires.nextElement().getName();
				
				if(name.endsWith("/")) continue;
				else if(name.startsWith("textures")) { fileLists.get(AssetTypes.Texture).add("/" +name.split("/")[1]); }
				else if(name.startsWith("lang")) { fileLists.get(AssetTypes.Language).add("/" +name.split("/")[1]); }
				else if(name.startsWith("shaders")) { fileLists.get(AssetTypes.Shader).add("/" + name.split("/")[1].split("\\.")[0]); }
				else if(name.startsWith("materials")) { fileLists.get(AssetTypes.Material).add("/" + name.split("/")[1].split("\\.")[0]); }
				else if(name.startsWith("sheets")) { fileLists.get(AssetTypes.SpriteSheet).add("/" + name.split("/")[1].split("\\.")[0]); }
				else if(name.startsWith("models")) { fileLists.get(AssetTypes.Mesh).add("/" +name.split("/")[1]); }
				else if(name.startsWith("audio")) { fileLists.get(AssetTypes.AudioClip).add("/" + name.split("/")[1].split("\\.")[0]); }
			}
			jar.close();
		}
		
		if(externalDir == null) return;
		fileLists.get(AssetTypes.Texture).addAll(importFromExternalDir("textures", 1, externalDir));
		fileLists.get(AssetTypes.Language).addAll(importFromExternalDir("lang", 1, externalDir));
		fileLists.get(AssetTypes.Shader).addAll(importFromExternalDir("shaders", 0, externalDir));
		fileLists.get(AssetTypes.Material).addAll(importFromExternalDir("materials", 0, externalDir));
		fileLists.get(AssetTypes.Sprite).addAll(importFromExternalDir("sprites", 0, externalDir));
		fileLists.get(AssetTypes.SpriteSheet).addAll(importFromExternalDir("sheets", 0, externalDir));
		fileLists.get(AssetTypes.AudioClip).addAll(importFromExternalDir("audio", 0, externalDir));
		fileLists.get(AssetTypes.Mesh).addAll(importFromExternalDir("models", 1, externalDir));
	}

	private static void importFromDir(String externalDir) throws IOException {
    	LoggingUtil.coreLog(Severity.Trace, "Import from dir/eclipse: ");
		// Loads engine resources from folders
		fileLists.get(AssetTypes.Texture).addAll(importFromLocalDir("textures", 1));
		fileLists.get(AssetTypes.Language).addAll(importFromLocalDir("lang", 1));
		fileLists.get(AssetTypes.Shader).addAll(importFromLocalDir("shaders", 0));
		fileLists.get(AssetTypes.AudioClip).addAll(importFromLocalDir("audio", 0));
		fileLists.get(AssetTypes.Mesh).addAll(importFromLocalDir("models", 1));
		fileLists.get(AssetTypes.Material).addAll(importFromLocalDir("materials", 0));
		fileLists.get(AssetTypes.Sprite).addAll(importFromLocalDir("sprites", 0));
		fileLists.get(AssetTypes.SpriteSheet).addAll(importFromLocalDir("sheets", 0));
		
		// Load other assets from external folders
		if(externalDir == null) return;
		fileLists.get(AssetTypes.Texture).addAll(importFromExternalDir("textures", 1, externalDir));
		fileLists.get(AssetTypes.Language).addAll(importFromExternalDir("lang", 1, externalDir));
		fileLists.get(AssetTypes.Shader).addAll(importFromExternalDir("shaders", 0, externalDir));
		fileLists.get(AssetTypes.AudioClip).addAll(importFromExternalDir("audio", 0, externalDir));
		fileLists.get(AssetTypes.Mesh).addAll(importFromExternalDir("models", 1, externalDir));
		fileLists.get(AssetTypes.Material).addAll(importFromExternalDir("materials", 0, externalDir));
		fileLists.get(AssetTypes.Material).addAll(importFromExternalDir("sprites", 0, externalDir));
		fileLists.get(AssetTypes.Material).addAll(importFromExternalDir("sheets", 0, externalDir));
	}
	
	private static List<String> importFromLocalDir(String path, int useExt) throws IOException {
    	LoggingUtil.coreLog(Severity.Trace, "Import local dir: " + path);
		List<String> paths = new ArrayList<String>();
		InputStream in = cl.getResourceAsStream(path);
		if(in == null) {
			LoggingUtil.coreLog(Severity.Warning, "When importing assets from jar folder was not found: " + path);
			return paths;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		
		while((line = br.readLine()) != null) {
			if(useExt == 1)
				paths.add("/" + line);
			else
				paths.add("/" + line.split("\\.")[0]);
		}
		
		in.close();
		br.close();
		return paths;
	}
	
	private static List<String> importFromExternalDir(String path, int useExt, String externalDir) {
    	LoggingUtil.coreLog(Severity.Trace, "Import external dir: " + path);
		List<String> paths = new ArrayList<String>();
		File dir = new File(externalDir + path);
		File[] files = dir.listFiles();
		if(files == null) return paths;
		for(int i = 0; i < files.length; i ++) {
			String aPath = files[i].getAbsolutePath();
			if(aPath.endsWith("/")) continue;
			if(useExt == 0) aPath = aPath.split("\\.")[0];
			paths.add(aPath);
		}
		return paths;
	}
	
}
