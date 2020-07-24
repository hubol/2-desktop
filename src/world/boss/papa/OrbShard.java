package world.boss.papa;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class OrbShard extends Entity{
	private Shake s = new Shake();
	private boolean special = false;

	public OrbShard(double x, double y) {
		this(x, y, false);
	}
	
	public OrbShard(double x, double y, boolean b){
		super(x, y);
		
		sprite = Sprite.get("sPapaOrbShard");
		hspeed = Calc.rangedRandom(3);
		vspeed = -3 - Calc.random(6);
		imageSpeed = 0;
		imageSingle = 0;
		
		if (b){
			setDirSpeed(45 + Calc.random(90), 6 + Calc.random(5));
			special = true;
		}
		
		xscale = .9 + Calc.random(.2);		
		angle = Calc.random(360);
		
		setDepth(-24);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void step(){
		if (special){
			if (hspeed > 0)
				hspeed += .05;
			else
				hspeed -= .05;
			hspeed *= .9;
			vspeed *= 1.01;
			vspeed += 1;
		}
		else{
			hspeed *= .995;
			vspeed += .8;
		}
		
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
