package dk.sebsa.ironflask.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class SocketThread extends Thread {
	private ServerSocket socket;
	private Consumer<Socket> consumer;
	
	public SocketThread(ServerSocket socket, Consumer<Socket> consumer) {
		this.socket = socket;
		this.consumer = consumer;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				@SuppressWarnings("resource")
				Socket clientSocket = socket.accept();
				consumer.accept(clientSocket);
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
}
