package view;

import javax.swing.JTabbedPane;

import controller.GUIListener;

public class UnitsTaps extends JTabbedPane {
	
	private UnitsPanel availableUnitsPnl,respondingUnitsPnl,treatingUnitsPnl;
	public UnitsTaps (GUIListener listener)
	{
		availableUnitsPnl = new UnitsPanel(listener);
		respondingUnitsPnl = new UnitsPanel(listener);
		treatingUnitsPnl = new UnitsPanel(listener);
		addTab("Available units", availableUnitsPnl);
		addTab("Responding units", respondingUnitsPnl);
		addTab("Treating units", treatingUnitsPnl);
		addKeyListener(ESCButtonListener.getInstance());
		setMaximumSize(availableUnitsPnl.getMaximumSize());
	}
	public UnitsPanel getAvailableUnitsPnl() {
		return availableUnitsPnl;
	}
	public UnitsPanel getRespondingUnitsPnl() {
		return respondingUnitsPnl;
	}
	public UnitsPanel getTreatingUnitsPnl() {
		return treatingUnitsPnl;
	}

}
