package dk.sebsa.ironflask.engine.utils;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;

public class CommandUtils {
	private final static List<Command> commands = new ArrayList<>();
	private static int init = 0;
	
	public static void init() {
		if(init == 1) return;
		
		commands.add(new Command(CommandUtils::commandDelte, "delete"));
		
		init = 1;
	}
	
	public static void addCommand(Command cmd) {
		commands.add(cmd);
	}
	
	public static void runCommad(String cmd) {
		String[] commandStrings = cmd.split(" ");
		String[] args = new String[commandStrings.length-1];
		for(int i = 1; i < commandStrings.length; i++) {
			args[i-1] = commandStrings[i];
		}
		String command = commandStrings[0];
		
		for(Command c : commands) {
			try {
				if(c.name.equals(command)) c.consumer.accept(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
