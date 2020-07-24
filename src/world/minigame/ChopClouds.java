package world.minigame;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class ChopClouds extends Entity{
	public double toAlpha, approach;

	public ChopClouds(double x, double y) {
		super(x, y);
		
		setDepth(Integer.MIN_VALUE+16);
		
		sprite = Sprite.get("sTreeClouds");
		imageSpeed = 0;
		imageSingle = Calc.random(4);
		
		xscale = (3 + Calc.random(8)) * (Double)Calc.choose(1.0, -1.0);
		yscale = 3 + Calc.random(8);
		
		alpha = 0;
		toAlpha = Calc.random(1);
		approach = 4 + Calc.random(16);
		
		hspeed = Calc.rangedRandom(2.2);
		
		orientation = Sprite.CENTERED;
	}
	
	public void step(){
		super.step();
		
		alpha = Calc.approach(alpha, toAlpha, approach);
		
		if (x < 0)
			x += 640;
		else if (x > 640)
			x-= 640;
		
		x += Calc.rangedRandom(.4);
		y += Calc.rangedRandom(.4);
		
		if (y > 360)
			y = 360;
		else if (y < 120)
			y = 120;
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x, y, xscale, yscale, 0, alpha, 1, 1, 1);
		sprite.render(imageSingle, orientation, x + 640, y, xscale, yscale, 0, alpha, 1, 1, 1);
		sprite.render(imageSingle, orientation, x - 640, y, xscale, yscale, 0, alpha, 1, 1, 1);
	}

}
