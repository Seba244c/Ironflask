package dk.sebsa.ironflask.engine.core;

import java.util.ArrayList;
import java.util.List;

public class AssetManager {
	public static List<Asset> allAssets = new ArrayList<>();
	
	public static void cleanup() {
		for(Asset a : allAssets) {
			System.out.println(a.name);
			a.cleanup();
		}
	}
}
