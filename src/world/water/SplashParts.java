package world.water;

import main.Calc;
import graphics.Graphics;
import graphics.Shape;
import world.control.Global;

public class SplashParts extends WaterMom{
	public double mult;

	public SplashParts(double x, double y, double dir, double spd, double scale) {
		super(x, y);
		xscale = scale + Calc.rangedRandom(1);
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		mult = .8 + Calc.random(.09);
		
		setDirSpeed(dir, spd);
	}
	
	public void step(){
		xscale *= mult;
		xscale -= .009525;
		if (xscale <= 0)
			destroy();
		super.step();
		vspeed += .8;
	}
	
	public void renderBack(double x, double y, double img){
		Graphics.setColor(Global.bloodOutlineColor);
		Graphics.setAlpha(1);
		Shape.drawCircle(this.x + x, this.y + y, (8 * xscale) + 3);
	}
	
	public void renderFront(double x, double y, double img){
		Graphics.setColor(Global.bloodColor);
		Graphics.setAlpha(1);
		Shape.drawCircle(this.x + x, this.y + y, 8 * xscale);
	}

}
