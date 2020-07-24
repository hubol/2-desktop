package world.boss.jelly;

import main.Calc;
import graphics.Sprite;

public class JellyBubbleSmall extends Jelly{
	
	//isnt there a less shit way of doing this??? i literally dont remember
	public JellyBubbleSmall(double x, double y){
		super(x, y);
		init(3);
	}

	public JellyBubbleSmall(double x, double y, double s) {
		super(x, y);
		init(s);
	}
	
	public void init(double s){
		setDepth(-2);
		
		sprite = JellyBubble.BUBBLE;
		backSprite = JellyBubble.BUBBLEBACK;
		
		orientation = Sprite.CENTERED;

		setDirSpeed(Calc.random(360), Calc.random(s));
		
		xscale = (1.0 - Calc.random(.3))*.7;
		yscale = xscale;
	}
	
	public void step(){
		setDirSpeed(getDirection(),getSpeed()*.99);
		
		super.step();

		xscale *= .995;
		yscale = xscale;
		xscale -= .00243515;
		
		if (xscale <= 0)
			destroy();
	}
	
	public void render(){
		
	}

}
