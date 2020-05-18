package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GUIHelper {
	public static Color SIMI_BLACK = new Color(51, 56, 64);
	public static Color SIMI_WHITE = new Color(173, 175, 179);
	public static Color BUILDING_COLOR = new Color (245,203,92);
	public static Color CITIZEN_COLOR = new Color (109,211,206);
	public static Color FIRE_BUILDING = new   Color(217,93,57);
	public static Color BLOOD_LOSS = new  Color(197,34,51);
	public static Color GAS_LEAK_BUILDING = new Color(3,206,164);
	public static Color COLLAPSE_BUILDING = new Color(0,0,34);
	public static Color DECEASED_CITIZEN = new Color(15,17,12);
	public static Color TREATING = new Color(68,157,209);
	public static Color RESPONDING = new Color(68,161,160);
	public static Color SERVER = new Color(244,241,187);
	public static Color CLIENT = new Color(237,106,90);

	public static JButton makeImageButton(String path) {
		try {	
			BufferedImage buttonIcon = ImageIO.read(new File(path));
			JButton button = new JButton(new ImageIcon(buttonIcon));
			button.setBackground(SIMI_BLACK);
			return button;
		} catch (IOException e) {
			System.out.println("Can't read the image");
			e.printStackTrace();
			return null;
		}
	}
	
	public static JButton makeScalledImageButton(String path,Dimension size) {
		try {	
			BufferedImage buttonIcon = ImageIO.read(new File(path));
			Image img =buttonIcon.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
			JButton button = new JButton(new ImageIcon(img));
			button.setBackground(SIMI_BLACK);
			return button;
		} catch (IOException e) {
			System.out.println("Can't read the image");
			e.printStackTrace();
			return null;
		}
	}
	public static class ExitListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}
}
