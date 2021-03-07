package dk.sebsa.ironflask.engine.io;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.io.LoggingUtil.Severity;

public class Log {
	public String text;
	public String time;
	public Severity severity;
	public boolean core;
	
	public static List<Log> logs = new ArrayList<>();
	
	public Log(String text, String time, Severity severity, boolean isCore) {
		this.text = text;
		this.time = time;
		this.severity = severity;
		this.core = isCore;
		
		if(logs.size() >= 50) logs.remove(49);
		logs.add(0, this);
	}
	
	public String toString() {
		if(core)
			return "[" + time + "] " +"CORE: " + text;
		else
			return "[" + time + "] " +"APP: " + text;
	}
}
