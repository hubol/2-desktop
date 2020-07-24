package world.gameplay;

import world.Bg;
import world.control.Global;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

/**these blocks default to being disabled and are enabled when an AddBlockEvent object tells them so*/

public class AddBlock extends Entity{

	public AddBlock(double x, double y) {
		super(x, y);
		orientation=Sprite.NORTHWEST;
		setDepth(10);
		sprite=Global.sBLOCK;
		mask=sprite.mask;
		inactive();
		visible=false;
	}
	
	public void active(){
		alpha = 1;
		setCollisionGroup(Global.SOLIDBLOCK, Global.BLOCK); //solid block group
	}
	
	public void inactive(){
		alpha = .5;
		setCollisionGroup(0); //shit group
	}
	
	public void step(){
		visible=!(Bg.me.bgVis);
		
		if (alpha == 1)
			setCollisionGroup(Global.SOLIDBLOCK, Global.BLOCK);
		else
			setCollisionGroup(0);
		
		if (Calc.pointDistance(x + 16, y + 16, Player.me.x, Player.me.y) < 96 && alpha == 1){
			if (Scene.collision(this, x, y, Global.PLAYER)){
				Player.hurtPlayer(125);
				setCollisionGroup(0);
			}
		}
	}

}
