package world.particles;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class SpeedSparks extends Entity{

	public SpeedSparks(double x, double y, double h) {
		super(x, y);
		sprite = Sprite.get("sWallDust");
		orientation = Sprite.CENTERED;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(1);
		
		hspeed = h;
		vspeed = Calc.rangedRandom(.5);
		
		imageSpeed = .08 + Calc.random(.32);
		imageSingle = 0;
		
		xscale = 1 + Calc.rangedRandom(.25);
		yscale = xscale;
		
		angle = Calc.random(360);
		
		setColor(Calc.getColorHSV((int)Calc.random(256), 255, 255));
	}
	
	public void step(){
		x += Calc.rangedRandom(.5) + hspeed;
		y += Calc.rangedRandom(.5) + vspeed;
		angle += Calc.rangedRandom(2);
		
		hspeed *= .95;
		
		imageSingle += imageSpeed;
		if (imageSingle >= 4){
			visible = false;
			destroy();
		}
	}
	
	public void render(){
		sprite.render((int)imageSingle, orientation, x + Calc.rangedRandom(.5), y + Calc.rangedRandom(.5), xscale, yscale, angle, 1, 1,1,1);
		sprite.render((int)imageSingle + 4, orientation, x + Calc.rangedRandom(.5), y + Calc.rangedRandom(.5), xscale, yscale, angle, 1, colR, colG, colB);
	}

}
