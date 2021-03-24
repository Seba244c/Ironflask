package dk.sebsa.ironflask.networking.server;

import dk.sebsa.ironflask.networking.Connection;

public class Packet {
	public String string;
	public Connection connection;
	public ClientHandler clientHandler;
	
	public Packet(ClientHandler clientHandler, String inputLine) {
		this.clientHandler = clientHandler;
		this.connection = clientHandler.connection;
		this.string = inputLine;
	}
}
