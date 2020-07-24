package world.boss.slick;

import world.control.Sound;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class SlickRockDebris extends Entity{
	public boolean fuck;

	public SlickRockDebris(double x, double y) {
		super(x, y);
		sprite = Sprite.get("sSlickRockDebris");
		imageSingle = (int)Calc.random(4);
		imageSpeed = 0;
		hspeed = Calc.rangedRandom(7.5);
		vspeed = -4 - Calc.random(6);
		
		angle = Calc.random(360);
		
		fuck = false;
		
		setDepth(-4);
	}
	
	public void step(){
		if (!fuck){
			vspeed += .8;
			hspeed *= .96;
		}
		else{
			alpha -= .02;
			if (alpha <= 0)
				destroy();
		}
		super.step();
		if (y >= 378 && vspeed > 0 && !fuck){
			Sound.playPitched("sSlickDebrisLand", .5);
			hspeed = 0;
			vspeed = 0;
			fuck = true;
		}
		
		if (y >= 480)
			destroy();
	}

}
