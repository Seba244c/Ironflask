package dk.sebsa.ironflask.engine.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.enums.*;
import dk.sebsa.ironflask.engine.utils.FileUtil;

public class LoggingUtil {
	public static boolean traceLog = false;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("d/MM-u");
	private static String logString = "";
	public static final String editorVersion = LoggingUtil.class.getPackage().getImplementationVersion();
	
	private static String getTime() {
		return dtf.format(LocalDateTime.now());
	}
	
	public static void appLog(Application a, Severity s, Object o) {
		Log log = new Log(o.toString(), getTime(), s, false);
		System.out.println(log.toString());
		logString += log.toString() + "\n";
	}
	
	public static void coreLog(Severity s, Object o) {
		Log log = new Log(o.toString(), getTime(), s, true);
		System.out.println(log.toString());
		logString += log.toString() + "\n";
	}
	
	public static void saveToFile() {
		String fullLog = "# Log from ironflask\n";
		fullLog += "# "+dtf2.format(LocalDateTime.now());
		fullLog += "\n# V: " + editorVersion + "\n";
		fullLog += logString;
		
		try {
			FileWriter myWriter = new FileWriter("./latest.log", false);
			myWriter.write(fullLog);
			myWriter.close();
			FileUtil.zipSingleFile(Paths.get("./latest.log"), "log.zip", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
