package controller;

import javax.swing.JButton;

import model.units.Unit;

public interface GUIListener {
	void nextCycle();
	void onUnitSelected(JButton btn);
	void onUnSelectUnit();
	void onCellSelected(JButton btn);
	void onSendMessageClicked(String message,Boolean isFromClient);
	void onAskForHelp();
}
