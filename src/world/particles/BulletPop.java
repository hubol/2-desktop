package world.particles;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class BulletPop extends Entity{
	
	public final static Sprite SPRITE = Sprite.get("sGunPop");

	public BulletPop(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-15);
		
		sprite = SPRITE;
		orientation = Sprite.CENTERED;
		angle = Calc.random(360);
		xscale = .9 + Calc.rangedRandom(.1);
		xscale *= (Integer)Calc.choose(1, -1);
		yscale = xscale;
		imageSpeed = (6.0 + Calc.random(6.0)) / 30.0;
		
		hspeed = Calc.rangedRandom(.1);
		vspeed = Calc.rangedRandom(.1);
	}
	
	public void step(){
		x += Calc.rangedRandom(imageSingle * 2);
		y += Calc.rangedRandom(imageSingle * 2);
		super.step();
		if (imageSingle >= 3)
			super.destroy();
	}
	
	public void render(){
		super.render();
	}

}
