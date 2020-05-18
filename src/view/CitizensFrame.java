package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import controller.GUIListener;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Injury;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Rescuable;

public class CitizensFrame extends JFrame{
	private ArrayList<Citizen> citizens;
	private GUIListener guiListener;
	public CitizensFrame (GUIListener guiListener,ArrayList<Citizen> citizens)
	{
		this.citizens = citizens;
		this.guiListener = guiListener;
		setTitle("Buidling Occupants");
		setSize(600,154);
		setResizable(false);
		getContentPane().setBackground(GUIHelper.SIMI_BLACK);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(GUIHelper.SIMI_BLACK);
		buttonPanel.setLayout(new FlowLayout());
		for (Citizen citizen : citizens) {
			JButton button = new JButton(citizen.getName());
			button.setForeground(Color.WHITE);
			button.setFocusable(false);
			setColor(button, citizen);
			button.setToolTipText("<html>" + citizen.toString().replaceAll("\n", "<br>") + "</html>");
			setColor(button, citizen);
			button.putClientProperty("target", citizen);
			button.putClientProperty("location", citizen.getLocation());
			button.setPreferredSize(new Dimension(90,100));
			button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					guiListener.onCellSelected(button);
					CitizensFrame.this.dispose();
					
				}
			});
			buttonPanel.add(button);
		}
		JScrollPane scrollPane =  new JScrollPane(buttonPanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane);
		revalidate();
		setVisible(true);
	}
	private void setColor(JButton btn, Rescuable rescuable) {
		boolean isCitizen = rescuable instanceof Citizen;
		Color color;
		String iconPath = null;
		if (rescuable.getDisaster() == null || !rescuable.getDisaster().isActive()) {
			if (isCitizen)
			{
				color = GUIHelper.CITIZEN_COLOR;
				Citizen citizen = (Citizen) rescuable;
				if(citizen.getState() == CitizenState.DECEASED)
					color = GUIHelper.DECEASED_CITIZEN;
			}
			else
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
}
