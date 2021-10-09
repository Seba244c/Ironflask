package dk.sebsa.ironflask.launcher;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
	public static File configFile;
	public static File usrDir;
	public static boolean newUsr = false;
	public static Properties properties;
	
	public static void init() {
		usrDir = new File(System.getProperty("user.dir") + "/usr/");
		usrDir.mkdir();
		
		configFile = new File(System.getProperty("user.dir") + "/usr/config.properties");
		newUsr = !configFile.exists();
	}
	
	public static void loadConfig() throws IOException {
		if (newUsr) {
			FileReader fr = new FileReader(configFile);
			Properties p = new Properties();
			p.load(fr);
			fr.close();
		}
	}
}
