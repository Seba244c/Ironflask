package dk.sebsa.ironflask.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import dk.sebsa.ironflask.networking.Connection;

public class GameServer implements Runnable {
	private ServerSocket serverSocket;
	private Supplier<Boolean> isRunning;
	private List<ClientHandler> clients = new ArrayList<>();
	private List<Packet> packets = new ArrayList<>();
	
	public void init(int port, Supplier<Boolean> isRunning) {
		serverSocket = null;
		this.isRunning = isRunning;
		
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Opened socket on port: " + serverSocket.getLocalPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + serverSocket.getLocalPort());
            System.exit(1);
        }
	}
	
	@SuppressWarnings("deprecation")
	public void close() throws IOException {
		System.out.println(" >> Closing clients");
		for (ClientHandler clientHandler : clients) {
			System.out.println(" >>> "+ clientHandler.connection.socket);
			clientHandler.close();
			clientHandler.stop();
		}
		
		System.out.println(" >> Closing socket");
		serverSocket.close();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// Create thread for new connections
		SocketThread socketThread = new SocketThread(serverSocket, this::input);
		Thread thread = new Thread(socketThread);
		thread.start();
		
		List<ClientHandler> removeList = new ArrayList<>();
		while (isRunning.get()) {
			if(packets.isEmpty()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) { e.printStackTrace(); }
				continue;
			}
			
			for (Packet packet : packets) {				
				System.out.println("\n" + packet.connection.socket);
				System.out.println(" >> " + packet.string);
			}
			packets.clear();
			removeList.clear();
		}
		
		// stop it all
		System.out.println("Closing server");
		System.out.println(" >> Closing socket thread");
		
		thread.stop();
		
		try {
			close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void getPacket(Packet p) {
		packets.add(p);
	}
	
	public void input(Socket socket) {
		System.out.println("\nNew connection was made:\n >> " + socket);
		
		try {
			ClientHandler newClient = new ClientHandler(Connection.generate(socket), this::getPacket);
			clients.add(newClient);
			newClient.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getPort() {
		return serverSocket.getLocalPort();
	}
}
