package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneLayout;

import controller.GUIListener;
import model.units.Unit;

public class UnitsPanel extends JPanel implements ActionListener {
	private JPanel content;
	private JScrollPane scrollPane;
	private JButton selectedButton;
	private GUIListener listener;
	public UnitsPanel(GUIListener listener) {
		this.listener = listener;
		content = new JPanel();
		content.setAutoscrolls(true);
		setBackground(GUIHelper.SIMI_BLACK);
		content.setBackground(GUIHelper.SIMI_BLACK);

		addKeyListener(ESCButtonListener.getInstance());
		content.setPreferredSize(new Dimension(480, 300));
		scrollPane = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(480, 80));
		setMaximumSize(content.getPreferredSize());
		add(scrollPane);
		revalidate();
	}

	public void updateUnits(ArrayList<JButton> btns) {
		content.removeAll();
		for (JButton btn : btns) {
			btn.setFocusable(false);
			btn.addKeyListener(ESCButtonListener.getInstance());
			btn.addActionListener(this);
			content.add(btn);
		}
		content.revalidate();
		scrollPane.revalidate();
		revalidate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton unitBtn = (JButton) arg0.getSource();
		if (selectedButton != null && selectedButton != unitBtn) {
			selectedButton.setSelected(false);
			selectedButton.setBackground(GUIHelper.SIMI_BLACK);
		}
		unitBtn.setSelected(!unitBtn.isSelected());
		if (unitBtn.isSelected()) {
			unitBtn.setBackground(GUIHelper.SIMI_WHITE);
			selectedButton = unitBtn;
			listener.onUnitSelected(unitBtn);
		}
		else
		{
			unitBtn.setBackground(GUIHelper.SIMI_BLACK);
			selectedButton = null;
			listener.onUnSelectUnit();

		}

	}

}
