package world.dream;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class DreamHeartParticles extends Entity {
	public double timer, mult;
	public boolean special;

	public DreamHeartParticles(double x, double y, double dir, int depth) {
		super(x, y);
		setDepth(depth);
		setCollisionGroup(Global.DEACTIVATEME);
		sprite = Sprite.get("sHeart");
		imageSingle = Calc.random(2);
		imageSpeed = Calc.random(.3);
		
		orientation = Sprite.CENTERED;
		xscale = .1 + Calc.random(.8);
		yscale = xscale;
		
		mult = 0;
		
		setSpeed(2.5 + Calc.random(2.5));
		setDirection(dir);
		timer = 60 + Calc.random(120);
		
		special = Global.room(8,7);
	}
	
	public void step(){
		super.step();
		
		mult = Calc.approach(mult, 1, 4);
		
		vspeed -= .006125;
		
		if (imageSingle >= 2)
			imageSingle -= 2;
		
		timer -= 1;
		if (timer <= 0){
			alpha -= .007;
			if (alpha <= 0)
				destroy();
		}
		
		if (special && y < 227){
			setDepth(Integer.MIN_VALUE + 19);
			special = false;
		}
		
		if (x <= -64 || x >= 704 || y <= -64 || y>= 544)
			destroy();
	}
	
	public void render(){
		int i = (int)imageSingle;
		if (i == 0)
			i = (Integer)Calc.choose(0,2,4,6);
		else
			i = (Integer)Calc.choose(1,3,5,7);
		
		sprite.render(i, orientation, x + Calc.rangedRandom(Calc.random(1)), y + Calc.rangedRandom(Calc.random(1)), xscale * mult, yscale * mult, getDirection() - 90, alpha, "ffffff");
	}

}
