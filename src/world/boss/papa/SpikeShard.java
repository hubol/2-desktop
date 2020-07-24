package world.boss.papa;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class SpikeShard extends Entity{
	private Shake s = new Shake();

	public SpikeShard(double x, double y) {
		super(x, y);
		
		sprite = Sprite.get("sPapaSpikeShard");
		hspeed = Calc.rangedRandom(3);
		vspeed = -3 - Calc.random(6);
		imageSpeed = 0;
		imageSingle = 0;
		
		xscale = .9 + Calc.random(.2);		
		angle = Calc.random(360);
		
		setDepth(-14);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void step(){
		hspeed *= .995;
		vspeed += .8;
		angle += vspeed * Math.signum(hspeed) * .6;
		super.step();
		
		x += Calc.rangedRandom(.2);
		y += Calc.rangedRandom(.2);
		
		if (y > 540)
			destroy();
	}
	
	public void render(){
		sprite.render(0, Sprite.CENTERED, x + s.x + Calc.rangedRandom(.04), y + s.y + Calc.rangedRandom(.04), xscale, xscale, angle, 1, 1, 1, 1);
	}

}
