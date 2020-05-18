package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ESCButtonListener implements KeyListener{
	private static ESCButtonListener listener;
	//Singleton
	private ESCButtonListener()
	{
		
	}
	public static ESCButtonListener getInstance()
	{
		if(listener ==  null)
			listener = new ESCButtonListener();
		return listener;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
