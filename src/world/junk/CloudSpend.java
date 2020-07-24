package world.junk;

import graphics.Sprite;
import world.control.Global;
import main.Calc;
import main.Entity;

public class CloudSpend extends Entity{

	public CloudSpend(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(30);
		
		sprite = Sprite.get("sKissSpend");
		xscale = .4;
		yscale = .4;
		
		angle = Calc.random(360);
		setDirSpeed(angle, 6);
	}
	
	public void step(){
		setSpeed(getSpeed() * .92);
		
		alpha *= .9;
		alpha -= .005;
		
		y += vspeed + Calc.rangedRandom(.5);
		x += hspeed + Calc.rangedRandom(.5);
		
		if (alpha <= 0)
			destroy();
	}

}
