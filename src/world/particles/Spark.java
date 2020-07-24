package world.particles;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class Spark extends Entity {

	public Spark(double x, double y) {
		super(x, y);
		sprite = Global.sEXPLODE;
		orientation = Sprite.CENTERED;
		imageSpeed = .065 + Calc.random(.09);
		imageSingle = Calc.random(.15);
		angle = Calc.random(360);
		xscale = .25 + Calc.random(.25);
		yscale = xscale;
		setColor("ffffff");
		setSpeed(.5 + Calc.random(3.5));
		setDirection(Calc.random(360));
		
		setDepth(Integer.MIN_VALUE + 18);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		super.step();
		if (imageSingle >= 5)
			destroy();
	}
	
	public void render(){
		double zx = x, zy = y;
		x += -.5 + Calc.random(1);
		y += -.5 + Calc.random(1);
		super.render();
		setColor("ffffff");
		imageSingle += 6;
		super.render();
		setColor("ffffff");
		imageSingle -= 6;
		x = zx;
		y = zy;
	}

}
