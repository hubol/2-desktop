package world.boss.slick;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class SlickSpark extends Entity{
	public int life;
	public Shake s;

	public SlickSpark(double x, double y) {
		super(x, y);
		
		sprite = Sprite.get("sSlickSpark");
		imageSingle = Calc.random(5);
		imageSpeed = Calc.random(1.1);
		
		setDirSpeed(Calc.random(360), Calc.random(5));
		
		s = new Shake(.45);
		
		angle = Calc.random(360);
		
		life = 3 + (int)Calc.random(8);
		
		setDepth(-2);
		setCollisionGroup(Global.DEACTIVATEME);
		color();
	}
	
	public void color(){
		if (Calc.random(1) < .5)
			setColor("7AD9FF");
		else
			setColor("FFFF32");
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void step(){
		x += Calc.rangedRandom(.25);
		y += Calc.rangedRandom(.25);
		
		int i = (int)imageSingle;
		super.step();
		if ((int)imageSingle != i)
			angle = Calc.random(360);
		
		alpha = .8 + Calc.random(.4);
		
		life -= 1;
		if (life <= 0)
			destroy();
		else
			color();
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + Calc.rangedRandom(.2) + s.x, y + Calc.rangedRandom(.2) + s.y, 1, 1, angle, alpha, colR, colG, colB);
	}

}
