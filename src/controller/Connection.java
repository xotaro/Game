package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Connection extends Thread{
	protected Socket socket;
	protected BufferedReader reader;
	protected PrintWriter writer; 
	private MessagesListener messagesListener;
	private boolean isFromClient;
	public Connection(MessagesListener listener,boolean isFromClient)
	{
		this.isFromClient = isFromClient;
		this.messagesListener = listener;
	}
	public void writeLine(String line)
	{
		if(writer != null)
		{
			writer.println(line);
			writer.flush();
		}
	}
	public void startReading()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(reader !=  null)
				{
					String line = null;
					try {
						while( (line = reader.readLine()) != null)
						{
							messagesListener.onMessageRecived(line, isFromClient);
						}
					} catch (IOException e) {
						
					}
				}
			}
		}).start();
	}
	public Socket getSocket() {
		return socket;
	}
	
	
}
