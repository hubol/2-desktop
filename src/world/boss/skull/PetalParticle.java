package world.boss.skull;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class PetalParticle extends Entity{
	public double sub;

	public PetalParticle(double x, double y) {
		super(x, y);
		setDepth(-11);
		
		sprite = Sprite.get("sSkullPetalParticle");
		angle = Calc.random(360);
		xscale = (.55 + Calc.random(.25)) * (Double)Calc.choose(1.0,-1.0);
		yscale = (.55 + Calc.random(.25)) * (Double)Calc.choose(1.0,-1.0);
		setDirSpeed(Calc.random(360), .1 + Calc.random(1.5));
		sub = 1.0 / (5.0 + Calc.random(25));
	}
	
	public void step(){
		alpha -= sub;
		super.step();
		if (alpha <= 0)
			destroy();
	}

}
