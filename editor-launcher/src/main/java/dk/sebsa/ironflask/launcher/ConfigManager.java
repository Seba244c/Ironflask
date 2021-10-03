package dk.sebsa.ironflask.launcher;

import java.io.File;

public class ConfigManager {
	public static File configFile;
	public static File usrDir;
	public static boolean newUsr = false;
	
	public static void init() {
		usrDir = new File(System.getProperty("user.dir") + "/usr/");
		usrDir.mkdir();
		
		configFile = new File(System.getProperty("user.dir") + "/usr/config.properties");
		newUsr = !configFile.exists();
	}
}
