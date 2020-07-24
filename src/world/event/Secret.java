package world.event;

import world.Fg;
import world.control.Global;
import graphics.Sprite;
import main.Entity;
import main.Scene;

public class Secret extends Entity{

	public Secret(double x, double y) {
		super(x, y);
		visible = false;
		orientation = Sprite.NORTHWEST;
		mask = Sprite.get("sSecret").mask;
		setCollisionGroup(Global.SECRET);
		
		if (Scene.collision(this, x, y, Global.PLAYER)){
			Fg.touchSecret = true;
			Fg.me.secretAlpha = 0;
		}
	}
	
	public void step(){
		if (Scene.collision(this, x, y, Global.PLAYER))
			Fg.touchSecret = true;
	}

}
