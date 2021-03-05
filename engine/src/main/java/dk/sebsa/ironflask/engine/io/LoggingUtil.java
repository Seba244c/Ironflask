package dk.sebsa.ironflask.engine.io;

import dk.sebsa.ironflask.engine.Application;

public class LoggingUtil {
	public enum Severity {
		Trace,
		Info,
		Warning,
		Error
	}
	
	public static void appLog(Application a, Severity s, Object o) {
		System.out.println(o);
	}
	
	public static void coreLog(Severity s, Object o) {
		System.out.println(o);
	}
}
