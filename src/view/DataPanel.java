package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;


abstract public class  DataPanel extends JPanel {
	
	private JTextArea textArea;
	public DataPanel (String title,String des)
	{
		setBackground(GUIHelper.SIMI_BLACK);
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		textArea.setBackground(GUIHelper.SIMI_BLACK);
		textArea.setEditable(false);
		textArea.setForeground(Color.WHITE);
		textArea.setSize(getMaximumSize());
		textArea.setText(des);
		textArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		setBorder(BorderFactory.createTitledBorder(null, title, 0, 0, getFont(), Color.WHITE));
		JScrollPane pane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);		pane.setBackground(GUIHelper.SIMI_BLACK);
		pane.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 0));
		add(pane);
		
		textArea.addKeyListener(ESCButtonListener.getInstance());
		pane.addKeyListener(ESCButtonListener.getInstance());
		addKeyListener(ESCButtonListener.getInstance());
	}
	protected void updateData(String data,boolean deleteLast)
	{
		if(deleteLast)
			textArea.setText(data);
		else
			if(!textArea.getText().contains(data))
				textArea.setText(textArea.getText() + "\n" + data);
	}
}
