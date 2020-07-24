package world.event;

import graphics.Sprite;
import world.control.Global;
import world.control.NewGen;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class BathShit extends Entity{
	public NewGen gen;
	public double atY, toY, ap, remain;
	public double atS, toS, bp, bemain;
	public Shake s;

	public BathShit(double x, double y) {
		super(x, y);
		gen = new NewGen((x * .052) + 2.1, (y * .053) + 1.2, (x + y) * 1.3, x * y, 1.21, (x + y) * 2.0);
		orientation = Sprite.NORTH;
		
		s = new Shake(.4);
		
		setDepth(-5);
		setCollisionGroup(Global.DEACTIVATEME);
		
		atY = -gen.random(28.0);
		ap = gen.random(12.0);
		toY = atY;
		remain = gen.random(60.0);
		
		atS = .9 + gen.random(.3);
		bp = 2 + gen.random(12.0);
		toS = atS;
		bemain = gen.random(60.0);
	}
	
	public void beset(){
		toS = .9 + gen.random(.3);
		bp = 2 + gen.random(12.0);
		bemain = gen.random(60.0);
	}
	
	public void step(){
		atY = Calc.approach(atY, toY, ap);
		remain -= 1;
		if (remain <= 0)
			reset();
		
		atS = Calc.approach(atS, toS, bp);
		bemain -= 1;
		if (bemain <= 0)
			beset();
	}
	
	public void reset(){
		toY = -gen.random(28.0);
		ap = 2 + gen.random(10.0);
		remain = gen.random(60.0);
	}
	
	public void render(){
		Sprite.get("sBathShit").render(0, orientation, x + s.x + Calc.rangedRandom(.25), y + s.y + Calc.rangedRandom(.25) + atY, .5 * atS, .5, Calc.rangedRandom(.5), 1, 1, 1, 1);
	}

}
