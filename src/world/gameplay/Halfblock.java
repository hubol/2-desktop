package world.gameplay;

import world.Bg;
import world.control.Global;
import graphics.Sprite;
import main.Entity;

public class Halfblock extends Entity{

	public Halfblock(double x, double y) {
		super(x, y);
		orientation=Sprite.NORTHWEST;
		setDepth(-10);
		sprite=Global.sUPBLOCK;
		mask=sprite.mask;
		setCollisionGroup(Global.SOLIDBLOCK, Global.BLOCK); //solid block group
		visible=false;
	}
	
	public void step(){
		visible=!(Bg.me.bgVis);
	}

}
