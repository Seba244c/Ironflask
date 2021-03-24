package dk.sebsa.ironflask.networking;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Client extends Thread {
    public Socket clientSocket;
    public PrintWriter out;
    public BufferedReader in;
    
    public abstract void startConnection(String ip, int port);
}
