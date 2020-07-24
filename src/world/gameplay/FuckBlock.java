package world.gameplay;

import world.Bg;
import world.control.Global;
import graphics.Sprite;
import main.Entity;
import main.Scene;

public class FuckBlock extends Entity{

	public FuckBlock(double x, double y) {
		super(x, y);
		orientation=Sprite.NORTHWEST;
		setDepth(-10);
		sprite=Global.sBLOCK;
		mask=sprite.mask;
		setCollisionGroup(Global.SOLIDBLOCK, Global.BLOCK); //solid block group
		visible=false;
	}
	
	public void step(){
		visible=!(Bg.me.bgVis);
		
		if (Scene.collision(this, x, y, Global.PLAYER))
			setCollisionGroup(0);
		else
			setCollisionGroup(Global.SOLIDBLOCK, Global.BLOCK);
	}

}
