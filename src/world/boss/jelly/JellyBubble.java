package world.boss.jelly;

import world.control.Global;
import world.control.Sound;
import main.Calc;
import main.Scene;
import graphics.Sprite;

public class JellyBubble extends Jelly{
	public int popTimer;
	public boolean popped;
	public boolean must, vis;
	
	public double addDir;
	public double life;
	
	public boolean woof = false;
	
	public final static Sprite BUBBLE = Sprite.get("sJellyBubble"), BUBBLEBACK = Sprite.get("sJellyBubbleBack"), POP = Sprite.get("sJellyPop"), POPBACK = Sprite.get("sJellyPopBack");
	
	//isnt there a less shit way of doing this??? i literally dont remember
	public JellyBubble(double x, double y){
		super(x, y);
		init(3);
	}

	public JellyBubble(double x, double y, double s) {
		super(x, y);
		init(s);
	}
	
	public JellyBubble(double x, double y, double s, boolean p) {
		super(x, y);
		init(s);
		if (p)
			sprite = Sprite.get("sJellyBombBubble");
	}
	
	public void init(double s){
		vis = false;
		
		woof = (Global.room(3,3) || Global.room(5, 3));
		
		setDepth(-2);
		setCollisionGroup(Global.DEACTIVATEME);
		
		sprite = BUBBLE;
		backSprite = BUBBLEBACK;
		
		orientation = Sprite.CENTERED;
		
		popped = false;
		popTimer = 0;
		
		setDirSpeed(Calc.random(360), Calc.random(s));
		addDir = Calc.rangedRandom(2.5);
		
		life = 30 + Calc.random(60);
		
		xscale = 1 - Calc.random(.15);
		yscale = xscale;
		
		must = false;
	}
	
	public void pop(){
		if (!popped){
			if ((Boolean)Calc.choose(false,false,false,false,false,true) || must)
				Sound.bubblesPlay();
			
			if (sprite == Sprite.get("sJellyBombBubble"))
				sprite = Sprite.get("sJellyBombPop");
			else
				sprite = POP;
			
			backSprite = POPBACK;
			
			popped = true;
			popTimer = 5;
			
			angle = Calc.random(360);
			
			setSpeed(getSpeed()/2.0);
		}
	}
	
	public void step(){
		if (popped){
			popTimer -= 1;
			if (popTimer <= 0)
				destroy();
		}
		
		setDirection(getDirection()+addDir);
		
		super.step();
		
		if (woof){
			if (Scene.collision(this, x, y, Global.SOLIDBLOCK, Global.BULLET)){
				must = true;
				pop();
			}
		}
		
		if (!popped){
			xscale *= .995;
			yscale = xscale;
			life -= 1;
			if (life <= 0)
				pop();
		}
	}
	
	public void render(){
	}

}
