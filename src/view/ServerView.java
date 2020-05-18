package view;

import java.awt.Color;

import controller.GUIListener;

public class ServerView extends ConnectionView {

	public ServerView(GUIListener guiListener, String titile) {
		super(guiListener, titile,false);
	}
	public Color getColor()
	{
		return GUIHelper.SERVER;
	}
}
