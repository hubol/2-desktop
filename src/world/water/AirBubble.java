package world.water;

import world.control.Global;
import graphics.Graphics;
import graphics.Shape;
import main.Calc;
import main.Scene;

public class AirBubble extends WaterMom{
	public double life, aprch, a, add, mult, size;

	public AirBubble(double x, double y) {
		super(x, y);
		life = 15 + Calc.random(40);
		aprch = -1.25 - Calc.random(1.25);
		
		sprite = Global.sBLOCK;
		mask = Global.sBLOCK.mask;
		xscale = 0.03125;
		yscale = 0.03125;
		
		size = 4 + Calc.random(8);
		
		a = 0;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		add = .5 + Calc.random(.5);
		mult = Calc.rangedRandom(8);
	}
	
	public void step(){
		a += add;
		x = xstart + (mult * Math.sin(a / 15.0));
		vspeed = Calc.approach(vspeed, aprch, 12);
		life -= 1;
		super.step();
		
		if (life <= 0){
			size *= .95;
			size -= .01;
			if (size < 6){
				size *= .95;
				size -= .04;
			}
		}
		
		if (!Scene.collision(this, x, y - (size / 2.0), Global.WATER)){
			size *= .5;
			size -= .05;
		}
		
		if (size <= 0)
			destroy();
	}
	
	public void renderBack(double x, double y, double img){
		
	}
	
	public void renderFront(double x, double y, double img){
		Graphics.setColor(Global.bloodOutlineColor);
		Graphics.setAlpha(1);
		Shape.drawCircle(this.x + x, this.y + y, (size / 2.0) + 3);
		
		Graphics.setColor(Global.bloodColor);
		Graphics.setAlpha(1);
		Shape.drawCircle(this.x + x, this.y + y, size / 2.0);
	}

}
