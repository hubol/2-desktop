package world.boss.jelly;

import main.Calc;
import graphics.Sprite;

public class JellyBubbleUp extends Jelly{

	public JellyBubbleUp(double x, double y) {
		super(x, y);

		setDepth(-2);
		
		sprite = JellyBubble.BUBBLE;
		backSprite = JellyBubble.BUBBLEBACK;
		
		orientation = Sprite.CENTERED;

		hspeed = Calc.rangedRandom(1);
		vspeed = -2 - Calc.random(5);
		
		xscale = .7 + Calc.random(.25);
		yscale = xscale;
	}
	
	public void step(){
		super.step();
		
		vspeed -= .78;
		vspeed *= 1.008;
		hspeed *= .98;

		xscale *= .98;
		yscale = xscale;
		xscale -= .00543515;
		
		x += Calc.rangedRandom(1);
		y += Calc.rangedRandom(1);
		
		if (xscale <= 0 || y <= 0)
			destroy();
	}
	
	public void render(){
		
	}

}
