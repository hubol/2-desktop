package world.event;

import world.control.Global;
import graphics.Sprite;
import main.Entity;

public class NoOccupy extends Entity{

	public NoOccupy(double x, double y) {
		super(x, y);
		orientation = Sprite.NORTHWEST;
		sprite = Global.sBLOCK;
		mask = sprite.mask;
		setCollisionGroup(Global.NOOCCUPY);
		visible = false;
	}
	
	public void step(){
		
	}
	
	public void render(){
		
	}

}
