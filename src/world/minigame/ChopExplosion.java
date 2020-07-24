package world.minigame;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class ChopExplosion extends Entity{
	public final double mult = 1.8;
	public String col;

	public ChopExplosion(double x, double y) {
		super(x, y);
		sprite = Global.sEXPLODE;
		orientation = Sprite.CENTERED;
		imageSpeed = .2 + Calc.random(.45);
		imageSingle = Calc.random(.15);
		angle = Calc.random(360);
		xscale = (.7 + Calc.random(.6)) * mult;
		yscale = xscale;
		col = (String)Calc.choose("FF3E30","FFFF47");
		setColor(col);
		setSpeed(1.5 + Calc.random(3));
		setDirection(Calc.random(360));
		
		setDepth(Integer.MIN_VALUE + 12);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		super.step();
		if (imageSingle >= 5)
			destroy();
	}
	
	public void render(){
		double zx = x, zy = y;
		x += -.5 + Calc.random(1);
		y += -.5 + Calc.random(1);
		super.render();
		setColor("ffffff");
		imageSingle += 6;
		super.render();
		setColor(col);
		imageSingle -= 6;
		x = zx;
		y = zy;
	}

}
