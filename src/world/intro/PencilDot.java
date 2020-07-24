package world.intro;

import graphics.Graphics;
import graphics.Shape;
import main.Calc;
import main.Entity;

public class PencilDot extends Entity{
	public Pencil mom;
	public double mult;

	public PencilDot(double x, double y, Pencil mommy) {
		super(x, y);
		mom = mommy;
		mult = 1;
	}
	
	public void step(){
		mult *= .7;
	}
	
	public void render(){
		Graphics.setColor(Intro.me.LINE);
		Graphics.setAlpha(1);
		double xx = Calc.rangedRandom(12 * mult), yy = Calc.rangedRandom(12 * mult);
		Shape.drawRectangle(x + xx + mom.s.x, y + yy + mom.s.y, x + 32 + xx + mom.s.x, y + 32 + yy + mom.s.y);
	}

}
