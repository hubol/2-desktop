package world.particles;

import graphics.Sprite;
import world.control.Global;
import main.Calc;
import main.Entity;

public class Stench extends Entity{
	public int phase;
	public double startDir, startSpd;

	public final static Sprite STENCH = Sprite.get("sStench");
	
	public Stench(double x, double y, double a) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-16);
		
		orientation = Sprite.CENTERED;
		
		sprite = STENCH;
		imageSingle = Calc.random(2);
		imageSpeed = Calc.random(.6);
		
		angle = a;
		alpha = 0;
		
		xscale = (.9 + Calc.random(.5))*((Double)Calc.choose(1.0,-1.0));
		yscale = Math.abs(xscale);
		
		startDir = 45 + Calc.random(90);
		startSpd = Calc.random(3.5);
		setDirSpeed(startDir + angle, startSpd);
		
		phase = 0;
	}
	
	public void step(){
		angle = Calc.approach(angle, 0, 40);
		setDirSpeed(startDir + angle, startSpd);
		startSpd *= .95;
		
		if (phase == 0){
			alpha += .2;
			if (alpha >= .8){
				alpha = .8;
				phase = 1;
			}
		}
		else{
			alpha -= .02;
			if (alpha <= 0)
				destroy();
		}
		super.step();
		
		x += Calc.rangedRandom(.3);
		y += Calc.rangedRandom(.3);
	}

}
