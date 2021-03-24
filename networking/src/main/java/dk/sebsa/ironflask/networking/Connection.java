package dk.sebsa.ironflask.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
	public PrintWriter out;
	public BufferedReader in;
	public Socket socket;
	
	public static Connection generate(Socket socket) throws IOException {
		Connection newConnection = new Connection();
		newConnection.socket = socket;
		newConnection.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		newConnection.out = new PrintWriter(socket.getOutputStream(), true);
		return newConnection;
	}
}
