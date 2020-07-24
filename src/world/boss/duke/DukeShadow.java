package world.boss.duke;

import world.control.Global;
import world.control.Shake;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class DukeShadow extends Entity{
	private boolean die = false;
	public Shake s = new Shake();

	public DukeShadow(double x, double y) {
		super(x, y);
		setDepth(20);
		
		sprite = Sprite.get("sDukeShadow");
		setCollisionGroup(Global.DEACTIVATEME);
		
		xscale = 0;
		yscale = 0;
		
		alarmInitialize(1);
		img();
	}
	
	public void alarmEvent(int i){
		img();
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void step(){
		if (!die)
			xscale = Calc.approach(xscale, 1, 2);
		else{
			xscale = Calc.approach(xscale,  0, 1.8);
			xscale -= .01;
			if (xscale <= 0)
				destroy();
		}
			
		yscale = xscale;
		super.step();
	}
	
	public void img(){
		imageSpeed = .15 + Calc.random(.03);
		alarm[0] = 1 + (int)Calc.random(30);
	}
	
	public void kill(){
		die = true;
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + s.x + Calc.rangedRandom(.1), y + s.y + Calc.rangedRandom(.1), xscale, yscale, angle, alpha, colR, colG, colB);
	}

}
