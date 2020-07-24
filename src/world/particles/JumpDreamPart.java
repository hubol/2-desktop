package world.particles;

import world.control.Global;
import graphics.Graphics;
import graphics.Shape;
import main.Calc;
import main.Entity;

public class JumpDreamPart extends Entity{
	public double size, mult;

	public JumpDreamPart(double x, double y, String color) {
		super(x, y);
		
		setDepth(10);
		setColor(color);
		
		mult = .3;
		size = 8 + Calc.random(8);
		setDirSpeed(Calc.random(360), Calc.random(1.9));
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		alpha = .9 + Calc.random(.1);
		alarmInitialize(1);
		alarm[0] = 15 + (int)Calc.random(10);
	}
	
	public void alarmEvent(int i){
		destroy();
	}
	
	public void step(){
		super.step();
		
		x += Calc.rangedRandom(1.5);
		y += Calc.rangedRandom(1.5);
		
		size *= .99;
		alpha *= .999;
		
		mult = Calc.approach(mult, 1, 1.3);
	}
	
	public void render(){
		Graphics.setAlpha(alpha);
		Graphics.setColor(colR, colG, colB);
		final double a = ((size * mult) / 2);
		Shape.drawRectangle(x - a, y - a, x + a, y + a);
		Graphics.setAlpha(1);
	}

}
