package world.gameplay;

import world.Bg;
import world.control.Global;
import world.player.Player;
import graphics.Sprite;
import main.Entity;

public class Upblock extends Entity{

	public Upblock(double x, double y) {
		super(x, y);
		orientation=Sprite.NORTHWEST;
		setDepth(10);
		sprite=Global.sUPBLOCK;
		mask=sprite.mask;
		setCollisionGroup(Global.SOLIDBLOCK); //non-solid upblock group (for enemies who walk on upblocks)
		visible=false;
	}
	
	public void step(){
		if (Global.playerY<y-15 && Player.me.vspeed >= 0)
			setCollisionGroup(Global.SOLIDBLOCK); //solid block group
		else
			setCollisionGroup(Global.UPBLOCK); //non-solid upblock group (for enemies who walk on upblocks)
		
		visible=!(Bg.me.bgVis);
	}

}
