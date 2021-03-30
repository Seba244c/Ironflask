package dk.sebsa.ironflask.engine.utils;

import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;

public class CommandUtils {
	public static void runCommad(String cmd) {
		String[] commandStrings = cmd.split(" ");
		String[] args = new String[commandStrings.length-1];
		for(int i = 1; i < commandStrings.length; i++) {
			args[i-1] = commandStrings[i];
		}
		String command = commandStrings[0];
		
		switch (command) {
		case "delete":
			commandDelte(args);
			break;
		}
	}
	
	public static void commandDelte(String[] args) {
		Entity e = WorldManager.getEntity(args[0]);
		if(e == null) {
			LoggingUtil.coreLog(Severity.Warning, "Entity(" + args[0] + ") does not exist!");
			return;
		}
		if(e.isDeletable()) e.delete();
	}
}
