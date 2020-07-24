package world.enemy;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class ToothShard extends Entity{

	public ToothShard(double x, double y, int i) {
		super(x, y);
		setDepth(-17);
		
		sprite = Sprite.get("sToothShatter");
		orientation = Sprite.CENTERED;
		
		imageSingle = i;
		imageSpeed = 0;
		
		hspeed = -2 - Calc.random(6);
		if (imageSingle == 1)
			hspeed *= -1.0;
		vspeed = -Calc.random(2);
		
		resetAngle();
	}
	
	public void step(){
		super.step();
		
		resetAngle();
		
		vspeed += .8;
		hspeed *= .98;
		
		x += Calc.rangedRandom(2);
		y += Calc.rangedRandom(2);
		
		if (y >= 520)
			destroy();
	}
	
	public void resetAngle(){
		if (imageSingle == 0)
			angle = getDirection() - 180;
		else
			angle = getDirection();
	}
	
	public void render(){
		super.render();
	}

}
