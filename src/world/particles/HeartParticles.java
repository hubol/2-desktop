package world.particles;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class HeartParticles extends Entity {
	public double timer;

	public HeartParticles(double x, double y) {
		super(x, y);
		setDepth(-15);
		setCollisionGroup(Global.DEACTIVATEME);
		sprite = Global.sHEART;
		imageSingle = Calc.random(2);
		imageSpeed = Calc.random(.3);
		
		orientation = Sprite.CENTERED;
		xscale = .1 + Calc.random(.8);
		yscale = xscale;
		
		setSpeed(2.5 + Calc.random(2.5));
		setDirection(Calc.random(360));
		timer = 60 + Calc.random(120);
	}
	
	public void step(){
		super.step();
		if (imageSingle >= 2)
			imageSingle -= 2;
		
		timer -= 1;
		if (timer <= 0){
			alpha -= .005;
			if (alpha <= 0)
				destroy();
		}
	}
	
	public void render(){
		
	}
	
	public void overRender(){
		int i = (int)imageSingle;
		if (i == 0)
			i = (Integer)Calc.choose(0,2,4,6);
		else
			i = (Integer)Calc.choose(1,3,5,7);
		
		sprite.render(i, orientation, x + Calc.rangedRandom(Calc.random(1)), y + Calc.rangedRandom(Calc.random(1)), xscale, yscale, getDirection() - 90, 1, "ffffff");
	}

}
