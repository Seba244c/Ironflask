package dk.sebsa.ironflask.engine.utils;

import java.util.function.Consumer;

public class Command {
	public Consumer<String[]> consumer;
	public String name;
	
	public Command(Consumer<String[]> consumer, String name) {
		this.name = name;
		this.consumer = consumer;
	}
}
