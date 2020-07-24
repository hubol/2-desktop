package world.junk;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class FruitExplode extends Entity{
	public double zspeed, to;

	public FruitExplode(double x, double y, int i){
		super(x, y);
		if (i == 0)
			sprite = Sprite.get("sStrawberry");
		else
			sprite = Sprite.get("sLemon");
		
		angle = Calc.random(360);
		
		setDepth(Integer.MIN_VALUE + 19);
		setCollisionGroup(Global.DEACTIVATEME);
		
		vspeed = -Calc.random(12);
		hspeed = Calc.rangedRandom(7);
		
		zspeed = -.001 + Calc.random(.007);
		
		to = .20 +Calc.random(.125);
		xscale = 0;
		yscale = xscale;
	}
	
	public void step(){
		x += hspeed + Calc.rangedRandom(.2);
		y += vspeed + Calc.rangedRandom(.2);
		angle += hspeed * Math.abs(vspeed) * 1.5;
		
		to += zspeed;
		xscale = Calc.approach(xscale, to, 3);
		
		vspeed += .6;
		hspeed *= .97;
		
		if (y > 500)
			destroy();
	}
	
	public void render(){
		sprite.render(0, orientation, x + Calc.rangedRandom(.3), y + Calc.rangedRandom(.3), xscale, xscale, angle, 1, 1, 1, 1);
	}

}
