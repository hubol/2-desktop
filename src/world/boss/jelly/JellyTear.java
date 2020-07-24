package world.boss.jelly;

import world.control.Global;
import world.control.Sound;
import main.Calc;
import main.Scene;
import graphics.Sprite;

public class JellyTear extends Jelly{
	public int popTimer;
	public boolean popped;
	public boolean must;
	
	//isnt there a less shit way of doing this??? i literally dont remember
	public JellyTear(double x, double y){
		super(x, y);
		
		setDepth(-2);
		setCollisionGroup(Global.DEACTIVATEME);
		
		sprite = Sprite.get("sJellyBubble");
		backSprite = Sprite.get("sJellyBubbleBack");
		
		orientation = Sprite.CENTERED;
		
		popped = false;
		popTimer = 0;
		
		xscale = .7 - Calc.random(.2);
		yscale = xscale;
		
		must = false;
		
		hspeed = (3 + Calc.random(4)) * ((Double)Calc.choose(1.0,-1.0));
		vspeed = -4.5 - Calc.random(2.7);
	}

	public void pop(){
		if (!popped){
			if ((Boolean)Calc.choose(false,false,false,false,false,true) || must)
				Sound.bubblesPlay();
			
			sprite = Sprite.get("sJellyPop");
			backSprite = Sprite.get("sJellyPopBack");
			
			popped = true;
			popTimer = 5;
			
			angle = Calc.random(360);
			
			setSpeed(getSpeed()/2.0);
		}
	}
	
	public void step(){
		if (popped){
			vspeed = Calc.approach(vspeed, 0, 2);
			popTimer -= 1;
			if (popTimer <= 0)
				destroy();
		}
		else
			vspeed += .8;
		
		hspeed *= .95;
		
		super.step();
		
		if (Scene.collision(this, x, y, Global.SOLIDBLOCK, Global.BULLET)){
			if (!Scene.collision(this, x, y, Global.JELLYBLOCK)){
				must = true;
				pop();
			}
		}
		
		if (y >= 416){
			y = 416;
			
			must = true;
			pop();
		}
		
		if (!popped){
			xscale *= .995;
			yscale = xscale;
		}
	}
	
	public void render(){
		
	}

}
