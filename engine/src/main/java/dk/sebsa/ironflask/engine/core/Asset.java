package dk.sebsa.ironflask.engine.core;

import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;

public abstract class Asset {
	public abstract void cleanup();
	public String name;
	
	public Asset(String name) throws AssetExistsException {
		this.name = name;
    	LoggingUtil.coreLog(Severity.Trace, "Creating asset named: "+name);
		if(name.contains("/")) this.name = name.split("/")[name.split("/").length-1];
		else if(name.contains("\\")) this.name = name.split("\\\\")[name.split("\\\\").length-1];
		if(AssetManager.exists(this.name)) throw new AssetExistsException();
		AssetManager.allAssets.add(this);
	}
	
	public Asset() {
		
	}
}
