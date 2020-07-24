package world.boss.skull;

import world.control.Global;
import main.Calc;
import main.Entity;

public class FlowerParticle extends Entity{
	public double mult, life;

	public FlowerParticle(double x, double y, double d) {
		super(x, y);
		setDirSpeed(d, 2 + Calc.random(7));
		mult = (Double)Calc.choose(1.0,-1.0);
		angle = Calc.random(360);
		sprite = Skull.me.flower;
		imageSingle = Calc.random(2);
		imageSpeed = 0;
		xscale = .4 + Calc.random(.4);
		yscale = xscale;
		setDepth(Integer.MIN_VALUE + 18);
		setCollisionGroup(Global.DEACTIVATEME);
		life = 15 + Calc.random(60);
	}
	
	public void step(){
		super.step();
		double s = getSpeed();
		setSpeed(s*.98);
		angle += s*mult;
		vspeed += .4;
		life -= 1;
		if (life <= 0){
			alpha -= .02;
			if (alpha <= 0)
				destroy();
		}
		else if (y >= 516)
			destroy();
	}

}
