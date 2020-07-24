package world.event;

import world.control.Global;
import graphics.Sprite;
import main.Entity;

public class StoneBellOn extends Entity{

	public StoneBellOn(double x, double y) {
		super(x, y);
		visible = false;
		orientation = Sprite.NORTHWEST;
		mask = Sprite.get("sSecret").mask;
		setCollisionGroup(Global.STONEBELLON);
	}
	
	public void step(){
		
	}
	
	public void render(){
		
	}

}
