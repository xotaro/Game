
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.ByteOrder;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import controller.CommandCenter;
import controller.GUIListener;

public class MainScreen extends JFrame{
	
	
	//private CommandCenter controller;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JButton exitButton;
	private CityPanel cityPanel;
	private ControlPanel controlPanel;
	private GUIListener guiListener;
	private UnitsTaps unitsTaps;
	private InfoPanel infoPanel;
	private LogPanel logPanel;
	
	public MainScreen (GUIListener guiListener)
	{
		this.guiListener = guiListener;
		setTitle("Disasters");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(GUIHelper.SIMI_BLACK);
		addKeyListener(ESCButtonListener.getInstance());
		setLayout(new BorderLayout());
		setFocusable(true);
		//addScreenButtons();
		makeFullScreen();
		addGameControl();
		setVisible(true);
		addDataPanels();
		
	}
	private void makeFullScreen()
	{
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setUndecorated(true);
	}
	private void addScreenButtons()
	{
	    exitButton =  GUIHelper.makeImageButton("src\\close-circle.png");
	    exitButton.addActionListener(new GUIHelper.ExitListener());
	    exitButton.setBounds((int)exitButton.getSize().getWidth(),(int) exitButton.getSize().getHeight(),2650, 50);
	    exitButton.setFocusable(false);
	    add(exitButton);
		validate();
	}
	private void addGameControl()
	{
	
		JPanel gameControl =  new JPanel();
		gameControl.setBackground(GUIHelper.SIMI_BLACK);
		gameControl.setBorder(BorderFactory.createEmptyBorder(16, 16, 4, 4));
		gameControl.addKeyListener(ESCButtonListener.getInstance());
		gameControl.setLayout(new BoxLayout(gameControl,BoxLayout.Y_AXIS));
		gameControl.setPreferredSize(new Dimension(500,screenSize.height));
		
		controlPanel = new ControlPanel(guiListener);
		controlPanel.addKeyListener(ESCButtonListener.getInstance());
		
//		availableUnits =  new UnitsPanel(guiListener);
//		availableUnits.addKeyListener(ESCButtonListener.getInstance());
		unitsTaps =  new UnitsTaps(guiListener);
		cityPanel =  new CityPanel(guiListener);
		cityPanel.addKeyListener(ESCButtonListener.getInstance());
		
		
		gameControl.add(controlPanel);
		gameControl.add(unitsTaps);
		gameControl.add(cityPanel);
		add(gameControl,BorderLayout.LINE_START);
	}
	private void addDataPanels()
	{
		JPanel dataPanel =  new JPanel();
		dataPanel.setBackground(GUIHelper.SIMI_BLACK);
		dataPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 6, 0));
		dataPanel.setLayout(new GridLayout(2, 1));
		infoPanel = new InfoPanel();
		logPanel =  new LogPanel();
		dataPanel.add(infoPanel);
		dataPanel.add(logPanel);
		add(dataPanel);
		
	}
	public CityPanel getCityPanel() {
		return cityPanel;
	}
	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public UnitsPanel getAvailableUnitsPanel() {
		return unitsTaps.getAvailableUnitsPnl();
	}
	public UnitsPanel getRespondingUnitsPanel() {
		return unitsTaps.getRespondingUnitsPnl();
	}
	public UnitsPanel getTreatingUnitsPanel() {
		return unitsTaps.getTreatingUnitsPnl();
	}
	public InfoPanel getInfoPanel() {
		return infoPanel;
	}
	public LogPanel getLogPanel() {
		return logPanel;
	}
		
	
	
}
