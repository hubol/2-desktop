package world.boss.slick;

import graphics.Sprite;
import world.control.Sound;
import main.Calc;
import main.Entity;

public class SlickPop extends Entity{
	public double i, s;

	public SlickPop(double x, double y) {
		super(x, y);
		setDirSpeed(Calc.random(360), 3 + Calc.random(3));
		i = 0;
		s = .3 + Calc.random(.45);
		imageSpeed = 0;
		angle = Calc.random(360);
		Sound.playPitched("sSlickPop", .2);
		setDepth(-2);
	}
	
	public void step(){
		i += s;
		x += Calc.rangedRandom(.3);
		y += Calc.rangedRandom(.3);
		super.step();
		if (i >= 3)
			destroy();
	}
	
	public void render(){
		Sprite.get("sSlickPop").render(i, orientation, x + Calc.rangedRandom(.25), y + Calc.rangedRandom(.25), 1, 1, angle, 1, 1, 1, 1);
	}

}
