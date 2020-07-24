package world.boss.jelly;

import world.control.Global;
import graphics.Sprite;
import main.Calc;

public class JellyExplosion extends Jelly {
	public String col;

	public JellyExplosion(double x, double y) {
		super(x, y);
		sprite = Global.sEXPLODE;
		orientation = Sprite.CENTERED;
		imageSpeed = .2 + Calc.random(.45);
		imageSingle = Calc.random(.15);
		angle = Calc.random(360);
		xscale = .7 + Calc.random(.6);
		yscale = xscale;
		col = "ff8b10";
		setColor(col);
		setSpeed(1.5 + Calc.random(3));
		setDirection(Calc.random(360));
		
		setDepth(-20);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public JellyExplosion(double x, double y, boolean p) {
		super(x, y);
		sprite = Global.sEXPLODE;
		orientation = Sprite.CENTERED;
		imageSpeed = .2 + Calc.random(.45);
		imageSingle = Calc.random(.15);
		angle = Calc.random(360);
		xscale = .7 + Calc.random(.6);
		yscale = xscale;
		if (!p)
			col = "ff8b10";
		else
			col = "FF4873";
		setColor(col);
		setSpeed(1.5 + Calc.random(3));
		setDirection(Calc.random(360));
		
		setDepth(-20);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		x += Calc.rangedRandom(.2);
		y += Calc.rangedRandom(.2);
		
		super.step();
		if (imageSingle >= 5)
			destroy();
	}
	
	public void renderBack(){
		sprite.render((int)imageSingle + 6, orientation, x + Calc.rangedRandom(.6), y + Calc.rangedRandom(.6), xscale, yscale, angle, alpha, "ffffff");
	}
	
	public void renderFront(){
		super.renderFront();
		sprite.render((int)imageSingle + 6, orientation, x + Calc.rangedRandom(.6), y + Calc.rangedRandom(.6), xscale, yscale, angle, alpha, "ffffff");
	}
	
	public void render(){

	}

}
