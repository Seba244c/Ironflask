package dk.sebsa.ironflask.engine.io;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.enums.*;

public class LoggingUtil {
	public static boolean traceLog = false;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("d/MM-u");
	public static final String editorVersion = LoggingUtil.class.getPackage().getImplementationVersion();
	
	private static String getTime() {
		return dtf.format(LocalDateTime.now());
	}
	
	public static void appLog(Application a, Severity s, Object o) {
		System.out.println(new Log(o.toString(), getTime(), s, false).toString());
	}
	
	public static void coreLog(Severity s, Object o) {
		System.out.println(new Log(o.toString(), getTime(), s, true).toString());
	}
	
	public static void saveToFile() {
		String fullLog = "# Log from ironflask\n";
		fullLog += "# "+dtf2.format(LocalDateTime.now());
		fullLog += "\n# V: " + editorVersion + "\n";
		for(Log log : Log.logs) {
			fullLog += log.toString() + "\n";
		}
		
		try {
			FileWriter myWriter = new FileWriter("./latest.log", false);
			myWriter.write(fullLog);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
