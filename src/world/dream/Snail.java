package world.dream;

import main.Calc;
import graphics.Sprite;
import world.control.Global;
import world.enemy.BasicEnemy;

public class Snail extends BasicEnemy{
	public double timer;

	public Snail(double x, double y) {
		super(x, y);
		
		orientation = Sprite.SOUTH;
		sprite = Sprite.get("sSnail");
		imageSingle = 0;
		
		resetSpeed();
		setDepth(-1);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void resetSpeed(){
		hspeed = (Double)Calc.choose(.25, 0.0, -.25);
		if (hspeed == 0){
			imageSingle = 0;
			imageSpeed = 0;
		}
		else
			imageSpeed = 1.0 / 15.0;
		
		timer = Calc.random(100);
	}
	
	public void step(){
		turning();
		
		timer -= 1;
		if (timer <= 0)
			resetSpeed();
		
		super.step();
		
		if (hspeed != 0)
			xscale = Math.signum(hspeed);
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + Calc.rangedRandom(.3), y + Calc.rangedRandom(.3), -xscale * 1.2, 1.2, 0, 1, 1, 1, 1);
	}

}
