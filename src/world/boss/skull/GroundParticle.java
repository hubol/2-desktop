package world.boss.skull;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class GroundParticle extends Entity {

	public GroundParticle(double x, double y) {
		super(x, y);
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		sprite = Sprite.get("sSkullUnearth");
		setDepth(-13);
		
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
