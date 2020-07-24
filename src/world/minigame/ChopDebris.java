package world.minigame;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class ChopDebris extends Entity{
	public double desired;

	public ChopDebris(double x, double y, int img) {
		super(x, y);
		setDepth(Integer.MIN_VALUE+13);
		
		sprite = Sprite.get("sTreeDebris");
		imageSingle = img;
		imageSpeed = 0;
		
		angle = Calc.random(360);
		vspeed = -2 - Calc.random(7);
		hspeed = Calc.rangedRandom(6);
		
		desired = .8 + Calc.random(.25);
		
		xscale = .1;
		yscale = xscale;
	}
	
	public void step(){
		angle += (Math.max(1, Math.abs(hspeed)) * Math.max(1, Math.abs(hspeed))) * .5 * Math.signum(hspeed);
		
		super.step();
		
		vspeed += .8;
		hspeed *= .96;

		xscale = Calc.approach(xscale, desired, 1.5);
		yscale = xscale;
		
		if (y > 540)
			destroy();
	}

}
