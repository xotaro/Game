package view;

import java.awt.Color;

import controller.GUIListener;

public class ClientView extends ConnectionView {
	public ClientView(GUIListener guiListener, String titile) {
		super(guiListener, titile, true);
	}

	public Color getColor() {
		return GUIHelper.CLIENT;
	}
}
