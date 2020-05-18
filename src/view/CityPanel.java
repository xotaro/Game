package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import controller.CommandCenter;
import controller.GUIListener;

public class CityPanel extends JPanel implements ActionListener {
	
	private GUIListener listener;
	public CityPanel(GUIListener listener) {
		this.listener = listener;
		setLayout(new GridLayout(10, 10));
		setBackground(GUIHelper.SIMI_BLACK);
	}

	public void updateCells(JButton[][] cells) {
		removeAll();
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				JButton button = cells[i][j];
				button.setFocusable(false);
				button.addActionListener(this);
				add(button);
			}
		validate();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		listener.onCellSelected((JButton)arg0.getSource());
	}
}
