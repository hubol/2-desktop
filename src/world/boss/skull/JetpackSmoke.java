package world.boss.skull;

import world.control.Global;
import main.Calc;
import main.Entity;

public class JetpackSmoke extends Entity{
	public double approach, toAlpha, add;

	public JetpackSmoke(double x, double y) {
		super(x, y);
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE + 17);
		
		sprite = Skull.me.smoke;
		
		setDirSpeed(90 + Calc.rangedRandom(5), 3 + Calc.random(2.7));
		approach = Calc.random(4);
		toAlpha = .2 + Calc.random(.75);
		
		alpha = 0;
		xscale = .2 + Calc.random(.55);
		yscale = xscale;
		
		angle = Calc.random(360);
		add = Calc.rangedRandom(4);
	}
	
	public void step(){
		alpha = Calc.approach(alpha, toAlpha, approach * 12);
		
		super.step();
		
		toAlpha -= .00125;
		hspeed *= 1.0006125;
		vspeed -= .05;
		
		if (alpha <= 0 || y <= -30)
			destroy();
		
		angle += add;
	}

}
