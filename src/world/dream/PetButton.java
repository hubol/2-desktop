package world.dream;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class PetButton extends Entity{
	public boolean last, thiss;
	public Shake s;

	public PetButton(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME, Global.SOLIDBLOCK);
		last = false;
		thiss = false;
		s = new Shake(.3);
		
		orientation = Sprite.SOUTH;
		sprite = Sprite.get("sPetButton");
		mask = Sprite.get("sPetButtonMask").mask;
	}
	
	public void step(){
		last = false;
		if (thiss)
			last = true;
		thiss = Scene.collision(this, x, y-1, Global.PLAYER) && Player.me.vspeed == 0 && Player.me.y < y - 23;
		
		if (pressed()){
		}
		
		if (Global.playerY<y-23 && Player.me.vspeed >= 0)
			setCollisionGroup(Global.SOLIDBLOCK); //solid block group
		else
			setCollisionGroup(Global.UPBLOCK); //non-solid upblock group (for enemies who walk on upblocks)
		
	}
	
	public boolean pressed(){
		return !last && thiss;
	}
	
	public void render(){
		double x = this.x + Calc.rangedRandom(.4) + s.x;
		double buttonY = y;
		if ((Player.me.x > x - 28 && Player.me.x < x + 28 && Player.me.y >= y - 33 && Player.me.y < y - 23 && Player.me.vspeed > 0)||thiss){
			//buttonY = y + ((y - 17) - Player.me.y);
			buttonY = Player.me.y + 33;
		}
		sprite.render(0, orientation, x, buttonY, 1, 1, 0, 1, 1, 1, 1);
		sprite.render(1, orientation, x, y, 1, 1, 0, 1, 1, 1, 1);
	}

}
