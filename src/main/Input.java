package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener {
	public static final int maxKey = 128;
	public static final int maxButton = 4;
	
	private static boolean[] pressed;
	private static boolean[] clicked;
	
	public static int mouseX;
	public static int mouseY;
	
	public static int[] framePressed;
	public static int[] frameClicked;
	
	public Input(){
		pressed = new boolean[maxKey];
		clicked = new boolean[maxButton];
		
		framePressed = new int[maxKey];
		for (int i=0; i<maxKey; i++){
			framePressed[i] = 0;
		}
		
		frameClicked = new int[maxButton];
		for (int i=0; i<maxButton; i++){
			frameClicked[i] = 0;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if(k<maxKey)
			pressed[k] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		if(k<maxKey)
			pressed[k] = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {}//Garbage
	
	/**call this once per step!!!!*/
	public static void framePressRefresh(){
		for (int i=0; i<maxKey; i++){
			if (checkKey(i)){
				if (framePressed[i] == 0)
					framePressed[i] = 2;
				else
					framePressed[i] = 1;
			}
			else
				framePressed[i] = 0;
		}
		
		for (int i=0; i<maxButton; i++){
			if (checkButton(i)){
				if (frameClicked[i] == 0)
					frameClicked[i] = 2;
				else
					frameClicked[i] = 1;
			}
			else
				frameClicked[i] = 0;
		}
	}
	
	/**check if a key was JUST PRESSED on this frame*/
	public static boolean checkFrameKey(int keyCode){
		return framePressed[keyCode] == 2;
	}
	
	/**check if a button was JUST CLICKED on this frame*/
	public static boolean checkFrameButton(int btnCode){
		return frameClicked[btnCode] == 2;
	}
	
	/**check if a key is being pressed/held*/
	public static boolean checkKey(int keyCode){
		if(keyCode>=maxKey){
			System.err.println("Unsupported key");
			return false;
		}
			
		return pressed[keyCode];
	}
	
	/**check if ANY KEY is being held/pressed*/
	public static boolean checkAnyKey(){
		for (int i=0; i<maxKey; i++){
			if (checkKey(i))
				return true;
		}
		return false;
	}
	
	public static String inputString(){
		for (int i=0; i<maxKey; i++){
			int keyInt = i;
			if (checkKey(i) && ((keyInt>=65 && keyInt<=90)||(keyInt>=97 && keyInt<=122)))
				return ""+(char)keyInt;
		}
		 return "";
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent e) {
		int k = e.getButton();
		if(k<maxButton)
			clicked[k] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int k = e.getButton();
		if(k<maxButton)
			clicked[k] = false;
	}
	
	/**check if a mouse button is pressed*/
	public static boolean checkButton(int button){
		return clicked[button];
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
}
