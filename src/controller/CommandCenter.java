package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultEditorKit.InsertContentAction;

import org.omg.CORBA.CTX_RESTRICT_SCOPE;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulator;
import view.CitizensFrame;
import view.ClientView;
import view.ESCButtonListener;
import view.GUIHelper;
import view.MainScreen;
import view.ServerView;

public class CommandCenter implements SOSListener, GUIListener, LogListener, MessagesListener, ConnectionListener {

	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;
	private Unit selectedUnit;
	private Address selectedLocation;
	private MainScreen mainScreen;
	private Server server;
	private Client client;
	private ServerView serverView;
	private ClientView clientView;
	private boolean isLog;
	private String friendLogData;
	public CommandCenter() throws Exception {
		engine = new Simulator(this);
		engine.setLogListener(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		mainScreen = new MainScreen(this);
		buildCity();
		buildUnits();

		server = new Server(this, this, 16200); // Sorry for this stupid port
		server.start();

	}

	public void buildUnits() {
		mainScreen.getAvailableUnitsPanel().updateUnits(createUnits(UnitState.IDLE));
		mainScreen.getRespondingUnitsPanel().updateUnits(createUnits(UnitState.RESPONDING));
		mainScreen.getTreatingUnitsPanel().updateUnits(createUnits(UnitState.TREATING));
	}

	public void buildCity() {
		mainScreen.getCityPanel().updateCells(createCells());
	}

	public ArrayList<JButton> createUnits(UnitState state) {
		ArrayList<JButton> btns = new ArrayList<JButton>();
		for (Unit u : emergencyUnits) {
			if (u.getState() == state) {
				JButton btn = GUIHelper.makeScalledImageButton(getImagePath(u), new Dimension(48, 48));
				btn.setToolTipText("<html>" + u.toString().replaceAll("\n", "<br>") + "</html>");
				btn.putClientProperty("unit", u);
				btns.add(btn);
			}
		}
		return btns;
	}

	private String getImagePath(Unit u) {
		if (u instanceof Ambulance)
			return "src\\ambulance.png";
		if (u instanceof DiseaseControlUnit)
			return "src\\diseaseUnit.png";
		if (u instanceof Evacuator)
			return "src\\police-van.png";
		if (u instanceof FireTruck)
			return "src\\fire-truck.png";

		return "src\\train.png";
	}

	public JButton[][] createCells() {
		JButton[][] btns = new JButton[10][10];
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				JButton button = new JButton();
				button.addKeyListener(ESCButtonListener.getInstance());
				button.setBorder(BorderFactory.createLineBorder(GUIHelper.SIMI_BLACK, 2));
				button.setBackground(Color.white);
				button.putClientProperty("location", new Address(i, j));

				btns[i][j] = button;
			}
		for (ResidentialBuilding building : visibleBuildings) {
			JButton button = btns[building.getLocation().getX()][building.getLocation().getY()];
			button.setToolTipText("<html>" + building.toString().replaceAll("\n", "<br>") + "</html>");
			button.putClientProperty("target", building);
			button.putClientProperty("location", building.getLocation());

			setColor(button, building);
		}
		for (Citizen citizen : visibleCitizens) {
			JButton button = btns[citizen.getLocation().getX()][citizen.getLocation().getY()];
			button.setToolTipText("<html>" + citizen.toString().replaceAll("\n", "<br>") + "</html>");
			setColor(button, citizen);
			button.putClientProperty("target", citizen);
			button.putClientProperty("location", citizen.getLocation());

		}
//		
		for (Unit unit : emergencyUnits) {
			if (unit instanceof Evacuator)
				continue;
			btns[unit.getLocation().getX()][unit.getLocation().getY()] = createUnitCell(btns, unit);
		}

		// Just to make sure that the evacuater will be shown if it was going from the
		// building to base
		for (Unit unit : emergencyUnits) {
			if (!(unit instanceof Evacuator))
				continue;
			btns[unit.getLocation().getX()][unit.getLocation().getY()] = createUnitCell(btns, unit);
		}
		return btns;
	}

	private JButton createUnitCell(JButton[][] btns, Unit unit) {
		JButton button = btns[unit.getLocation().getX()][unit.getLocation().getY()];
		// JButton unitBtn = GUIHelper.makeScalledImageButton(getImagePath(unit),new
		// Dimension(32, 32));
		try {
			BufferedImage buttonIcon;
			buttonIcon = ImageIO.read(new File(getImagePath(unit)));
			Image img = buttonIcon.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			if (unit.getState() == UnitState.TREATING)
				button.setBackground(GUIHelper.TREATING);
			else if (unit.getState() == UnitState.RESPONDING)
				button.setBackground(GUIHelper.RESPONDING);
			button.setIcon(new ImageIcon(img));
			button.putClientProperty("location", unit.getLocation());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return button;
	}

	private void setColor(JButton btn, Rescuable rescuable) {
		boolean isCitizen = rescuable instanceof Citizen;
		Color color;
		String iconPath = null;
		if (rescuable.getDisaster() == null || !rescuable.getDisaster().isActive()) {
			if (isCitizen) {
				color = GUIHelper.CITIZEN_COLOR;
				Citizen citizen = (Citizen) rescuable;
				if (citizen.getState() == CitizenState.DECEASED)
					color = GUIHelper.DECEASED_CITIZEN;
			} else
				color = GUIHelper.BUILDING_COLOR;
		} else {
			Disaster disaster = rescuable.getDisaster();
			if (disaster instanceof Collapse) {
				iconPath = "src\\coll.png";
				color = GUIHelper.COLLAPSE_BUILDING;
			} else if (disaster instanceof Fire) {
				color = GUIHelper.FIRE_BUILDING;
				iconPath = "src\\fire.png";
			} else if (disaster instanceof GasLeak) {
				iconPath = "src\\gasleak.png";
				color = GUIHelper.GAS_LEAK_BUILDING;
			} else if (disaster instanceof Injury) {
				iconPath = "src\\bloodloss.png";
				color = GUIHelper.BLOOD_LOSS;
			} else {
				iconPath = "src\\infection.png";
				color = GUIHelper.GAS_LEAK_BUILDING;
			}
		}
		btn.setBackground(color);
		if (iconPath != null) {
			try {
				btn.setIcon(new ImageIcon(ImageIO.read(new File(iconPath))));
			} catch (IOException e) {
				System.out.println("Cant read cell icon");
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			CommandCenter commandCenter = new CommandCenter();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void receiveSOSCall(Rescuable r) {
		if (r instanceof ResidentialBuilding) {
			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);
		} else {

			if (!visibleCitizens.contains(r)) {
				visibleCitizens.add((Citizen) r);
			}
		}

	}

	@Override
	public void nextCycle() {
		if (engine.checkGameOver()) {
			JOptionPane.showMessageDialog(null, "Game Over your score is: " + engine.calculateCasualties());
			System.exit(0);
			return;
		}
		try {
			engine.nextCycle();
			mainScreen.getCityPanel().updateCells(createCells());
			buildUnits();

			mainScreen.getControlPanel().updateCurrentCycle(engine.getCurrentCycle());
			mainScreen.getControlPanel().updateNumberOfCausalties(engine.calculateCasualties());
			if (selectedLocation != null)
				printCell(selectedLocation.getX(), selectedLocation.getY());
		} catch (CannotTreatException e) {

			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (BuildingAlreadyCollapsedException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());

		} catch (CitizenAlreadyDeadException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}

	@Override
	public void onUnitSelected(JButton btn) {
		Unit u = (Unit) btn.getClientProperty("unit");
		selectedUnit = u;
		mainScreen.getInfoPanel().updateData(u.toString());
		selectedLocation = null;
	}

	@Override
	public void onUnSelectUnit() {
		selectedUnit = null;
	}

	public void printCell(int x, int y) {
		String citizens = "";
		String buildings = "";
		String units = "";

		for (ResidentialBuilding building : visibleBuildings)
			if (building.getLocation().getX() == x && building.getLocation().getY() == y)
				buildings += applySpacesToString(building.toString() + "\n" + building.getOccupants().toString());
		if (buildings != "")
			buildings = "Buildings:\n" + buildings;

		for (Citizen citizen : engine.getCitizens())
			if (citizen.getLocation().getX() == x && citizen.getLocation().getY() == y)
				citizens += applySpacesToString(citizen.toString());
		if (citizens != "")
			citizens = "Citizens:\n" + citizens;

		for (Unit unit : emergencyUnits)
			if (unit.getLocation().getX() == x && unit.getLocation().getY() == y)
				units += applySpacesToString(unit.toString());
		if (units != "")
			units = "Units:\n" + units;

		String lines = buildings + citizens + units;
		if (lines != "")
			mainScreen.getInfoPanel().updateData(lines);
	}

	private String applySpacesToString(String s) {
		String[] lines = s.split("\n");
		String ss = lines[0] + "\n";
		for (int i = 1; i < lines.length; i++) {
			String line = lines[i];
			ss += String.format("      %s\n", line);
		}
		return ss;
	}

	@Override
	public void onCellSelected(JButton btn) {
		Object obj = btn.getClientProperty("target");
		Address location = (Address) btn.getClientProperty("location");
		selectedLocation = location;
		printCell(location.getX(), location.getY());
		if (obj != null) {
			Rescuable target = (Rescuable) obj;
			if (selectedUnit != null) {
				try {
					selectedUnit.respond(target);
					selectedUnit = null;
					buildUnits();
				} catch (CannotTreatException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Can't treat", JOptionPane.ERROR_MESSAGE);
				} catch (IncompatibleTargetException e) {
					if (target instanceof ResidentialBuilding) {
						ResidentialBuilding building = (ResidentialBuilding) target;
						if (building.getOccupants().size() == 0)
							JOptionPane.showMessageDialog(null, e.getMessage(), "Incompatabile target",
									JOptionPane.ERROR_MESSAGE);
						else
							askToTargetOccupents(building);

					}
				}
				;
			}
		}
	}

	public void askToTargetOccupents(ResidentialBuilding building) {
		int reply = JOptionPane.showConfirmDialog(null, "Incompatabile target, Do u want to target it's occupents?",
				"Target occupents", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			new CitizensFrame(this, building.getOccupants());
		}
	}

	@Override
	public void onCitizenDie(Citizen citizen) {

		String data = String.format("Citizen: %s has died in location %s and  in cycle %s",
				citizen.getName(), citizen.getLocation(), engine.getCurrentCycle());
		mainScreen.getLogPanel().updateData(data);
		updateFriendViewData(data);
		sendDataToFriend(data);

	}

	@Override
	public void onDiasterStrick(Disaster disaster) {
		String data = String.format("A new %s diaster has striked a %s in cycle %s",
				disaster.getClass().getSimpleName(), disaster.getTarget().getClass().getSimpleName(),
				engine.getCurrentCycle());
		mainScreen.getLogPanel()
				.updateData(data);
		updateFriendViewData(data);
		sendDataToFriend(data);

	}

	@Override
	public void onBuildingCollapsed(ResidentialBuilding building) {
		String data = String.format("Building at %s has been collapsed in cycle %s",
				building.getLocation(), engine.getCurrentCycle());
		mainScreen.getLogPanel().updateData(data);
		updateFriendViewData(data);
		sendDataToFriend(data);

	}
	public void updatePlayerViewData(String data)
	{
		if(serverView != null && serverView.isVisible())
		{
			serverView.updateLog(data);
		}
	}
	public void updateFriendViewData(String data)
	{
		if(clientView != null && clientView.isVisible())
		{
			clientView.updateLog(data);
		}
	}
	public void sendDataToFriend(String data)
	{
		
			client.writeLine("START_LOG" + data + "\nEND_LOG");
		
	}

	@Override
	public void onMessageRecived(String msg, boolean isFromClient) {
		if(msg.startsWith("START_LOG") && !isLog)
		{
			isLog = true;
			friendLogData = msg.replace("START_LOG", "");
			return;
		}
		if(isLog)
		{

			if(msg.equals("END_LOG"))
			{
				isLog = false;
				updatePlayerViewData(friendLogData);
				friendLogData =null;
			}
			else
				friendLogData += msg ;
			return;
		}
		msg = msg.trim();
		if (!isFromClient && !msg.equals("") )
		{
			serverView.addMessage("Player: " + msg,GUIHelper.CLIENT);
		}
		else
		{
			clientView.addMessage("Friend: " + msg,GUIHelper.SERVER);
		}
	}

	@Override
	public void onDeviceConnected(Server server) {
		serverView = new ServerView(this, server.getSocket().getInetAddress().getHostName());

	}

	@Override
	public void onSendMessageClicked(String message, Boolean isFromClient) {
		if (isFromClient)
		{
			client.writeLine(message);
			clientView.addMessage("You: " + message,GUIHelper.CLIENT);


		}
		else
		{
			server.writeLine(message);
			serverView.addMessage("You: " + message,GUIHelper.SERVER);

		}
	}

	@Override
	public void onAskForHelp() {
		String ip = JOptionPane.showInputDialog(null, "Enter Friend ip");
		if (ip != null && !ip.equals("")) {
			client = new Client(this, ip, 16200);
			client.start();
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				
				@Override
				public void run() {
					if(client.getSocket() != null)
					{
						clientView = new ClientView(CommandCenter.this, client.getSocket().getInetAddress().getHostName());		
						timer.cancel();
					}
				}
			}, 100, 100);
		}

	}

}
