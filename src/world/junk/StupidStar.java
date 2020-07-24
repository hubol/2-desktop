package world.junk;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class StupidStar extends Entity{
	private double ang, alp, alph;

	public StupidStar(double x, double y) {
		super(x, y);
		sprite = Sprite.get("sOrbStar");
		ang = Calc.rangedRandom(3);
		alp = Calc.random(.1);
		
		xscale = .3 + Calc.random(.2);
		yscale = xscale;
		angle = Calc.random(360);
		alph = .8 + Calc.random(.2);
		
		setDirSpeed(Calc.random(360), Calc.random(4));
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-2);
	}
	
	public void step(){
		setDirSpeed(getDirection(), getSpeed() * .95);
		angle += ang;
		
		alph *= .9;
		alph -= alp;
		if (alph <= 0)
			destroy();
		
		super.step();
		x += Calc.rangedRandom(1);
		y += Calc.rangedRandom(1);
	}

}
