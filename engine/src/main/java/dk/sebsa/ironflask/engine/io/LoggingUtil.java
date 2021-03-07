package dk.sebsa.ironflask.engine.io;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dk.sebsa.ironflask.engine.Application;

public class LoggingUtil {
	public static boolean traceLog = false;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
	public enum Severity {
		Trace,
		Info,
		Warning,
		Error
	}
	
	private static String getTime() {
		return dtf.format(LocalDateTime.now());
	}
	
	public static void appLog(Application a, Severity s, Object o) {
		if(s == Severity.Trace && !traceLog) return;
		System.out.println(new Log(o.toString(), getTime(), s, false).toString());
	}
	
	public static void coreLog(Severity s, Object o) {
		if(s == Severity.Trace && !traceLog) return;
		System.out.println(new Log(o.toString(), getTime(), s, true).toString());
	}
}
