package world.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SetControls implements KeyListener{

	@Override
	public void keyPressed(KeyEvent e) {
		if (Global.awaitKey)
			Global.keyReceive(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
