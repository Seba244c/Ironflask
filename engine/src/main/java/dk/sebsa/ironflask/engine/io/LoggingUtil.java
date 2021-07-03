package dk.sebsa.ironflask.engine.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.enums.*;
import dk.sebsa.ironflask.engine.utils.BuildUtil;
import dk.sebsa.ironflask.engine.utils.FileUtil;

public class LoggingUtil {
	public static boolean traceLog = false;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("d/MM-u");
	private static DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("d-MM-u");
	private static String logString = "";
	private static PrintStream printStream = new PrintStream(System.out);
	
	private static String getTime() {
		return dtf.format(LocalDateTime.now());
	}
	
	public static void appLog(Application a, Severity s, Object o) {
		Log log = new Log(o.toString(), getTime(), s, false);
		printStream.println(log.toString());
		logString += log.toString() + "\n";
	}
	
	public static void coreLog(Severity s, Object o) {
		Log log = new Log(o.toString(), getTime(), s, true);
		printStream.println(log.toString());
		logString += log.toString() + "\n";
	}
	
	public static void saveToFile(Application app) {
		String fullLog = "# Log from an Ironflask Application\n";
		fullLog += "# Build: " + BuildUtil.id + "\n";
		fullLog += "# OS: " + System.getProperty("os.name") + "\n";
		fullLog += "# DT: "+dtf2.format(LocalDateTime.now()) + "\n";
		fullLog += logString;
		
		try {
			// Create logs directory
			File logDirectory = new File("../logs/");
			if(!logDirectory.exists()) logDirectory.mkdir();
			
			FileWriter myWriter = new FileWriter("../logs/latest.log", false);
			myWriter.write(fullLog);
			myWriter.close();
			String zipName = "log-"+dtf3.format(LocalDateTime.now());
			zipName += "-"+getNumber(zipName);
			
			FileUtil.zipSingleFile(Paths.get("../logs/latest.log"), "../logs/" + zipName + ".zip", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getNumber(String start) {
		File directory = new File("../logs/");
		String[] files = directory.list();
		int i = 1;
		for(String s : files) {
			if(s.startsWith(start)) i++;
		}
		return i;
	}
	
	public static class RedirectErrPrintStream extends PrintStream {		
		public RedirectErrPrintStream(OutputStream out) {
	        super(out);
	    }
		
		@Override
		public void println(String stirng) {
			LoggingUtil.coreLog(Severity.Error, stirng);
		}
		
		@Override
		public void println(Object object) {
			LoggingUtil.coreLog(Severity.Error, String.valueOf(object));
		}
	}
	
	public static class RedirectPrintStream extends PrintStream {		
		public RedirectPrintStream(OutputStream out) {
	        super(out);
	    }
		
		@Override
		public void println(String stirng) {
			LoggingUtil.coreLog(Severity.Info, stirng);
		}
		
		@Override
		public void println(Object object) {
			LoggingUtil.coreLog(Severity.Info, String.valueOf(object));
		}
	}
}
