package world.intro;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class FallDust extends Entity{

	public FallDust(double x, double y, int multiplier) {
		super(x, y);
		sprite = Sprite.get("sIntroHubolDust");
		xscale = multiplier;
		hspeed = multiplier;
		
		setDepth(-1);
	}
	
	public void step(){
		hspeed *= 1.1;
		angle += Math.signum(hspeed) * .4;
		alpha -= .085;
		
		x += Calc.rangedRandom(.4);
		y += Calc.rangedRandom(.4);
		
		if (alpha <= 0)
			destroy();
		super.step();
	}
	
	public void render(){
		double x = this.x + Calc.rangedRandom(.2), y = this.y + Calc.rangedRandom(.2);
		sprite.render(1, Sprite.SOUTHWEST, x, y, xscale, 1, angle, alpha + .5, Intro.me.LINE);
		sprite.render(0, Sprite.SOUTHWEST, x, y, xscale, 1, angle, alpha + .5, Intro.me.BACK);
	}

}
