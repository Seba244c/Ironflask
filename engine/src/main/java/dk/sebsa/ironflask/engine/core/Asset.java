package dk.sebsa.ironflask.engine.core;

public abstract class Asset {
	public abstract void cleanup();
	public String name;
	
	public Asset(String name) {
		this.name = name;
		if(name.contains("/")) this.name = name.split("/")[name.split("/").length-1];
		else if(name.contains("\\")) this.name = name.split("\\\\")[name.split("\\\\").length-1];
		AssetManager.allAssets.add(this);
	}
}
