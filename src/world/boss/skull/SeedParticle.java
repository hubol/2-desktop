package world.boss.skull;

import graphics.Sprite;
import world.control.Global;
import main.Calc;
import main.Entity;

public class SeedParticle extends Entity{
	public int timer;
	public double div;

	public SeedParticle(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-8);
		
		sprite = Skull.me.seedPart;
		orientation = Sprite.CENTERED;
		
		timer = (int)Calc.random(6);
		angle = Calc.random(360);
		
		xscale = .35 + Calc.random(.35);
		yscale = xscale;
		
		div = 36 + Calc.random(26);
		
		setDirSpeed(90 + Calc.rangedRandom(80), 1 + Calc.random(2));
	}
	
	public void step(){
		timer -= 1;
		if (timer <= 0){
				angle = Calc.random(360);
				timer = 6;
		}
		
		super.step();
		
		x += Calc.rangedRandom(1);
		y += Calc.rangedRandom(1);
		
		xscale = Calc.approach(xscale, -.5, div);
		xscale -= .0001;
		if (xscale <= 0)
			destroy();
		yscale = xscale;
	}
	
	public void render(){
		double add = Math.min(1, xscale * xscale * 1.5);
		sprite.render(imageSingle, orientation, x + add, y, xscale, yscale, angle, 1, 1, 1, 1);
		sprite.render(imageSingle, orientation, x - add, y, xscale, yscale, angle, 1, 1, 1, 1);
		sprite.render(imageSingle, orientation, x, y + add, xscale, yscale, angle, 1, 1, 1, 1);
		sprite.render(imageSingle, orientation, x, y - add, xscale, yscale, angle, 1, 1, 1, 1);
		sprite.render(imageSingle, orientation, x, y, xscale, yscale, angle, 1, 1, 1, 1);
	}

}
