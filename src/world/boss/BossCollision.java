package world.boss;

import world.control.Global;
import graphics.Sprite;
import main.Entity;
import main.Scene;

public class BossCollision extends Entity{

	public BossCollision(double x, double y) {
		super(x, y);
		visible = false;
		orientation = Sprite.NORTHWEST;
		mask = Sprite.get("sSecret").mask;
		setCollisionGroup(Global.BOSSCOLLISION);
	}
	
	public void step(){
		if (Scene.collision(this, x, y, Global.PLAYER)){
			try {
				Global.bossCollision();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}