package dk.sebsa.ironflask.networking.server;

import java.io.IOException;
import java.util.function.Consumer;

import dk.sebsa.ironflask.networking.Connection;

class ClientHandler extends Thread {
    public Connection connection;
    private Consumer<Packet> packetConsumer;

    public ClientHandler(Connection connection, Consumer<Packet> packetConsumer) {
        this.connection = connection;
        this.packetConsumer = packetConsumer;
    }

    @Override
    public void run() {
        String inputLine;
        try {
			while ((inputLine = connection.in.readLine()) != null) {
		    	packetConsumer.accept(new Packet(this, inputLine));
			    if (".".equals(inputLine)) {
			        break;
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void close() {
    	connection.out.println(".");
    }
}
