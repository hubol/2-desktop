package world.minigame;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class ChopGem extends Entity{
	public boolean ugh;

	public ChopGem(double x, double y) {
		super(x, y);
		setDepth(Integer.MIN_VALUE+13);
		
		sprite = Sprite.get("sTreeMiniGem");
		imageSingle = Calc.random(3);
		imageSpeed = (7.0 + Calc.random(6)) / 30.0;
		
		angle = 0;//Calc.random(360);
		vspeed = -2 - Calc.random(7);
		hspeed = Calc.rangedRandom(6);
		
		xscale = .4;
		yscale = .4;
		
		ugh = false;
		
		//xscale = .1;
		//yscale = xscale;
	}
	
	public void step(){
		if (ugh){
			angle += (Math.max(1, Math.abs(hspeed)) * Math.max(1, Math.abs(hspeed))) * .5 * Math.signum(hspeed);
			
			super.step();
			
			vspeed += .8;
			hspeed *= .9;
		}
		else
			ugh = true;
		//xscale = Calc.approach(xscale, 1, 4.5);
		//yscale = xscale;
		
		if (y > 540)
			destroy();
	}

}
