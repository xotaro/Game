package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Connection {
	private int port;
	private boolean isFinished;
	private ConnectionListener connectionListener;
	public Server(MessagesListener listener,ConnectionListener connectionListener,int port) {
		super(listener,false);
		this.connectionListener = connectionListener;
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		while (!isFinished) {
			// Try with resources to make sure that we will close the server after we finish what we want
			try (ServerSocket server = new ServerSocket(port)){
				socket = server.accept();
				
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				connectionListener.onDeviceConnected(this);
				startReading();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void stopWorking() {
		isFinished = true;
	}

}
