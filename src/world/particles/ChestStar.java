package world.particles;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class ChestStar extends Entity{
	public boolean constant, special;
	public double addAngle, timer;

	public ChestStar(double x, double y) {
		super(x, y);
		setDepth(Integer.MIN_VALUE+2); //TODO change
		special = false;
		
		sprite = Sprite.get("sChestStars");
		color();
		constant = (Boolean)Calc.choose(false, false, false, false, false, true);
		
		setDirSpeed(Calc.random(360), 10 + Calc.random(16));
		addAngle = Calc.rangedRandom(10);
		
		angle = Calc.random(360);
		
		timer = Calc.random(120);
		
		xscale = .17 + Calc.random(.37);
		yscale = xscale;
		
		imageSingle = 0;
		imageSpeed = 0;
	}
	
	public ChestStar(double x, double y, boolean b) {
		super(x, y);
		setDepth(Integer.MIN_VALUE+2); //TODO change
		special = true;
		
		sprite = Sprite.get("sChestStars");
		color();
		constant = (Boolean)Calc.choose(false, false, false, false, false, true);
		
		setDirSpeed(90 + Calc.rangedRandom(20), 10 + Calc.random(16));
		addAngle = Calc.rangedRandom(10);
		
		angle = Calc.random(360);
		
		timer = Calc.random(120);
		
		xscale = .17 + Calc.random(.37);
		yscale = xscale;
		
		imageSingle = 0;
		imageSpeed = 0;
	}
	
	public void step(){
		if (constant)
			color();
		
		setSpeed(getSpeed() * 1.026);
		addAngle *= .96;
		xscale *= .97;
		yscale = xscale;
		
		angle += addAngle;
		
		timer -= 1;
		if (timer <= 0)
			destroy();
		
		x += Calc.rangedRandom(2);
		y += Calc.rangedRandom(2);
		
		super.step();
	}
	
	public void color(){
		setColor((String)Calc.choose("FFF832","9EFF21","32FF58","32FF8B","3AEBFF","5B63FF","9C63FF","DC5EFF","FF5BB5","FF233D","FF5826"));
	}

	public void render(){
		if (!special)
			r();
	}
	
	public void r(){
		super.render();
		sprite.render(1, Sprite.CENTERED, x, y, xscale, yscale, angle, 1, 1, 1, 1);
	}

}
