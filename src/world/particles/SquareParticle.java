package world.particles;

import world.control.Global;
import graphics.Graphics;
import graphics.Shape;
import main.Calc;
import main.Entity;

public class SquareParticle extends Entity{
	public double size;

	public SquareParticle(double x, double y) {
		super(x, y);
		size=5;
		setDepth(-15);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		x+= Calc.rangedRandom(.125);
		y+= Calc.rangedRandom(.125);
		
		size-=.08 + Calc.rangedRandom(.015);
		alpha-=.05 + Calc.rangedRandom(.015);
		setSpeed(getSpeed()*.97);
		
		super.step();
		
		if (size<=0 || alpha<=0)
			super.destroy();
	}
	
	public void render(){
		Graphics.setColor(colR,colG,colB);
		Graphics.setAlpha(alpha);
		Shape.drawRectangle(x-(size/2), y-(size/2), x+(size/2), y+(size/2));
		Graphics.setAlpha(1);
	}

}
