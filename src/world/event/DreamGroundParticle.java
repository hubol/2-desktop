package world.event;


import graphics.Sprite;
import main.Calc;
import main.Entity;

public class DreamGroundParticle extends Entity {

	public DreamGroundParticle(double x, double y) {
		super(x, y);
		
		sprite = Sprite.get("sMusicHeartUnearth");
		setDepth(-9);
		
		hspeed = Calc.rangedRandom(-3);
		vspeed = -Calc.random(6);
		if (Calc.random(1) > .6)
			vspeed *= 1 + Calc.random(.5);
		
		angle = Calc.random(360);
		xscale = .6 + Calc.random(.55);
		yscale = .6 + Calc.random(.55);
		xscale *= (Double)Calc.choose(1.0,-1.0);
		yscale *= (Double)Calc.choose(1.0,-1.0);
	}
	
	public void step(){
		super.step();
		hspeed *= .97;
		vspeed += .85;
		angle += hspeed;
		if (y > 422)
			destroy();
	}

}
