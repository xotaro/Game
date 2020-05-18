package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Connection{
	private String ip;
	private int port;
	
	public Client (MessagesListener listener,String ip, int port)
	{
		super(listener,true);
		this.ip = ip;
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}
	@Override
	public void run() {
		try {
			socket = new Socket(ip, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			startReading();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
