package dk.sebsa.ironflask.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import dk.sebsa.ironflask.networking.server.GameServer;
import dk.sebsa.ironflask.networking.test.GreetClient;

public class Main {
	public static boolean running = true;
	private static GameServer server;
	private static GreetClient testClient;
	
	public static void main(String[] args) throws IOException {
		// Init server
		server = new GameServer();
		Thread thread = new Thread(server);
		server.init(56636, Main::isRunning);
		thread.start();
		
		test();
		testClient.sendMessage("HELLO 2");

		testClient.start();

		// Close
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
		reader.readLine();
		
		reader.readLine();
		
		reader.close();
		running = false;
		
		// Close
		while (thread.isAlive() || !testClient.stop) {
			
		}
	}
	
	public static Boolean isRunning() {
		return running;
	}
	
	public static void test() {
		testClient = new GreetClient();
		testClient.startConnection("127.0.0.1", server.getPort());
		testClient.sendMessage("HELLO WORLD");
	}
}
