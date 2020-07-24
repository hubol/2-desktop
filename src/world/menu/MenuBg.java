package world.menu;

import world.control.Global;
import graphics.Graphics;
import graphics.Shape;
import main.Entity;

public class MenuBg extends Entity{

	public MenuBg() {
		super(0, 0);
		setDepth(4000);
	}
	
	public void step(){
		
	}
	
	public void render(){
		Graphics.setAlpha(1);
		Graphics.setColor(Global.menuBackgroundColor);
		Shape.drawRectangle(0, 0, 640, 480);
	}

}
