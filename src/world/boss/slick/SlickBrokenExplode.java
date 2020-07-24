package world.boss.slick;

import graphics.Sprite;
import world.control.Global;
import main.Calc;
import main.Entity;

public class SlickBrokenExplode extends Entity{
	public int kind; //0 = BODY, 1 = MANDIBLE, 2,3 = FOOT
	public int phase = 0;
	public double spd, toSpd, dir;
	
	//2 = rfoot, 3 = lfoot, 1 = rmand, 4 = lmand


	public SlickBrokenExplode(double x, double y, double a, int kind) {
		super(x, y);
		this.kind = kind;
		angle = a;
		
		if (kind != 0)
			Global.explosionEffect(x, y, .9);
		
		double dir = 90, mult = .2;
		if (kind > 0)
			mult = 1;
		if (kind == 1)
			dir = 45;
		else if (kind == 2)
			dir = 315;
		else if (kind == 3)
			dir = 225;
		else if (kind == 4)
			dir = 135;
		
		this.dir = dir;
		
		spd = 1.2;
		toSpd = (6 + Calc.random(3)) * mult;
		setDirSpeed(dir, spd);
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE + 10);
	}
	
	public void step(){
		super.step();
		
		if (phase == 1){
			hspeed *= .999;
			vspeed += .7;
		}
		else{
			spd = Calc.approach(spd, toSpd, 2.6) + .4;
			if (spd >= toSpd){
				phase = 1;
				spd = toSpd;
			}
			setDirSpeed(dir, spd);
		}
		angle += vspeed * Math.signum(hspeed) * .5;
	}
	
	public void render(){
		double xx = Calc.rangedRandom(.45), yy = Calc.rangedRandom(.45);
		if (kind == 0){
			Sprite.get("sSlickBody").render(0, orientation, x + xx, y + yy, 1, 1, angle, 1, 1, 1, 1);
			Sprite.get("sSlickBody").render(1, orientation, x + xx, y + yy, 1, 1, angle, 1, 1, 1, 1);
		}
		else if (kind == 1)
			Sprite.get("sSlickMandible").render(0, Sprite.SOUTH, x + xx, y + yy, 1, 1, angle, 1, 1, 1, 1);
		else if (kind ==2 || kind == 3)
			Sprite.get("sSlickFoot").render(0, Sprite.SOUTH, x + xx, y + yy, -1 + ((3 - kind) * 2), 1, angle, 1, 1, 1, 1);
		else if (kind == 4)
			Sprite.get("sSlickMandible").render(0, Sprite.SOUTH, x + xx, y + yy, -1, 1, angle, 1, 1, 1, 1);
	}

}
