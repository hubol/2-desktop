package world.event;

import world.Bg;
import world.control.Global;
import graphics.Sprite;
import main.Entity;

public class Teleport extends Entity{

	public Teleport(double x, double y) {
		super(x, y);
		orientation=Sprite.NORTHWEST;
		setDepth(10);
		sprite=Sprite.get("sTeleport");
		mask=sprite.mask;
		setCollisionGroup(Global.CONTROLLER);
		visible=false;
	}
	
	public void step(){
		visible=!(Bg.me.bgVis);
	}

}
