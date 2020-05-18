package view;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.TextArea;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class InfoPanel extends DataPanel {
	
	public InfoPanel ()
	{
		super("Info","No object is selected");
	}

	public void updateData(String data) {
		super.updateData(data, true);
	}
	
}
