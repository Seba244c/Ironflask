package dk.sebsa.ironflask.networking.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import dk.sebsa.ironflask.networking.Client;

public class GreetClient extends Client {
	public boolean stop = false;
	
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) { e.printStackTrace(); }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void stopConnection() {
    	System.out.println("GreetClient: Closing connection");
        try {
            in.close();
            out.close();
			clientSocket.close();
		} catch (IOException e) { e.printStackTrace(); }
    }
    
    @Override
    public void run() {
    	try {
    		String input;
    		while (!stop) {
    			input = in.readLine();
    			if(input.startsWith(".")) stop = true;
    		}
    		
    		stopConnection();
		} catch (IOException e) { e.printStackTrace(); }
    }
}
