package world.particles;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class Debris extends Entity{
	public double grav;

	public Debris(double x, double y, Sprite s) {
		super(x, y);
		sprite = s;
		orientation = Sprite.CENTERED;
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE + 19);
		
		imageSingle = Math.floor(Calc.random(3));
		imageSpeed = 0;
		angle = Calc.random(360);
		xscale = .45 + Calc.random(.1);
		yscale = xscale;
		
		vspeed = -.5 - Calc.random(6);
		hspeed = -8 + Calc.random(16);
		
		grav = .5 + Calc.random(.5);
		
		setColor("ffffff");
	}
	
	public void step(){
		vspeed += grav;
		super.step();
		hspeed *= .96;
		
		if (y>=500)
			super.destroy();
	}
	
	public void render(){
		double zx = x, zy = y;
		x += -.5 + Calc.random(1);
		y += -.5 + Calc.random(1);
		super.render();
		x = zx;
		y = zy;
	}

}
