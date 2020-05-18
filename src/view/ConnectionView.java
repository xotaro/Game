package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import controller.GUIListener;

public abstract class ConnectionView extends JFrame implements ActionListener {
	private JTextPane messagesPanel; 
	private JTextArea msgField;
	private boolean isFromClient;
	private GUIListener guiListener; 
	private LogPanel logPanel;
	public ConnectionView(GUIListener guiListener,String titile,boolean isFromClient)
	{
		this.isFromClient =isFromClient;
		this.guiListener = guiListener;
		setTitle(String.format("Connected to %s", titile));
		setSize(800,600);
		setLayout(new BorderLayout());
		JPanel chatPanel = new JPanel();
		chatPanel.setSize(300,getHeight());
		chatPanel.setMaximumSize(new Dimension(300,getHeight()));
		chatPanel.setPreferredSize(new Dimension(300,getHeight()));

		chatPanel.setLayout(new BorderLayout());
		setResizable(false);
		chatPanel.setLayout(new BorderLayout());
		JPanel sendingPanel = new JPanel();
		sendingPanel.setLayout(new FlowLayout());
		msgField = new JTextArea();
		sendingPanel.add(msgField);
		JButton nextBtn =  new JButton("Next");
		msgField.setPreferredSize(new Dimension(200,nextBtn.getPreferredSize().height));
		nextBtn.addActionListener(this);
		sendingPanel.add(nextBtn);
		chatPanel.add(sendingPanel,BorderLayout.SOUTH);
		messagesPanel = new JTextPane();
		messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
		getContentPane().setBackground(GUIHelper.SIMI_BLACK);
		messagesPanel.setBackground(GUIHelper.SIMI_BLACK);
		sendingPanel.setBackground(GUIHelper.SIMI_BLACK);
		sendingPanel.setBorder(BorderFactory.createLineBorder(GUIHelper.SIMI_WHITE));
		messagesPanel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		messagesPanel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		
		
		JScrollPane scrollPane = new JScrollPane(messagesPanel);
		
		chatPanel.add(scrollPane);
		chatPanel.setBackground(GUIHelper.SIMI_BLACK);
		add(chatPanel,BorderLayout.EAST);
		
		
		logPanel =  new LogPanel();
		logPanel.setMaximumSize(new Dimension(480,getHeight()));
		logPanel.setPreferredSize(new Dimension(480,getHeight()));
		
		add(logPanel,BorderLayout.WEST);
		setAlwaysOnTop(true);
		setVisible(true);
	}
	public void addMessage(String message,Color c)
	{
		appendToPane(messagesPanel, message + "\n", c);
		
		revalidate();
	}
	private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
	public void applySize(JTextArea text)
	{
		Graphics g = getGraphics();
		FontMetrics metrics = g.getFontMetrics(text.getFont());
		int hgt = metrics.getHeight();
		int adv = metrics.stringWidth(text.getText());
		Dimension size = new Dimension(adv+2, hgt+2);
		text.setMaximumSize(size);
		System.out.println(text.getText());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(msgField.getText().trim().equals(""))
			return;
		guiListener.onSendMessageClicked(msgField.getText().trim(), isFromClient);
		msgField.setText("");
	}
	public void updateLog(String data)
	{
		logPanel.updateData(data);
	}
	public abstract Color getColor();
	
}
