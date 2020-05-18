package controller;

public interface MessagesListener {
	void onMessageRecived(String msg,boolean isFromClient);
}
