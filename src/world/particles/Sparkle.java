package world.particles;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class Sparkle extends Entity{
	public double addAngle;
	
	public final static Sprite SPARKLE = Sprite.get("sSparkle");

	public Sparkle(double x, double y) {
		super(x, y);
		setDirSpeed(Calc.random(360),Calc.random(.2));
		
		orientation = Sprite.CENTERED;
		sprite = SPARKLE;
		
		angle = Calc.random(360);
		addAngle = Calc.rangedRandom(4);
		
		imageSpeed = .05 + Calc.random(.4);
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-10);
	}
	
	public void motion(double b){
		x += Calc.rangedRandom(.5 * b);
		y += Calc.rangedRandom(.5 * b);
	}
	
	public void step(){
		double mult = 1;
		
		angle += addAngle;
		super.step();
		
		if (imageSingle >= 2.5)
			mult += .35;
		if (imageSingle >= 3.5)
			mult += .75;
		if (imageSingle >= 4.5)
			mult += 1;
		
		motion(mult);
		
		if (imageSingle >= 5)
			destroy();
	}

}
