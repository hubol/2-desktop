package world.event;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class BloodFountain extends Entity{
	public double seed, a, b, div, mult;
	public Shake s;

	public BloodFountain(double x, double y, double ang) {
		super(x, y);
		angle = ang;
		seed = ((x *.96 * x) * .98) + ((y *.98 * y) * .96) - (ang * .69);
		
		sprite = Sprite.get("sBloodFountain");
		orientation = Sprite.SOUTH;
		setDepth(Integer.MIN_VALUE + 15);
		
		imageSingle = random(4);
		imageSpeed = .2 + random(.28);
		xscale = 1 + random(.12);
		yscale = 1 + random(.12);
		mult = random(1.95);
		div = 26 + random(6.1);
		a = random(div);
		
		b = mult * Math.sin(a / div);
		
		s = new Shake(.2);
		
		setCollisionGroup(Global.DEACTIVATEME);
		alarmInitialize(1);
		
		alarm[0] = 4 + (int)random(27);
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			imageSpeed = .2 + random(.28);
			xscale = 1 + random(.12);
			yscale = 1 + random(.12);
			mult = random(1.95);
			div = 26 + random(6.1);
			a = random(div);
			b = mult * Math.sin(a / div);
			alarm[0] = 4 + (int)random(27);
		}
	}
	
	public double random(double a){
		seed += a;
		seed *= a;
		if (seed >= Double.MAX_VALUE || seed <= Double.MIN_VALUE)
			seed = a;
		return ((seed % .8) / .8) * a;
	}
	
	public void step(){
		a += 1;
		b = mult * Math.sin(a / div);
		
		sprite = Sprite.get("sBloodFountain");
		super.step();
	}
	
	public void render(){
		Sprite.get("sBloodFountain").render(imageSingle, orientation, x + Calc.rangedRandom(.4) + s.y, y + Calc.rangedRandom(.4) + s.y, xscale, yscale, angle + b, 1, 1, 1, 1);
	}

}
