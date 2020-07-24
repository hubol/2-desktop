package world.enemy;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class ToothGhost extends Entity{

	public ToothGhost(double x, double y) {
		super(x, y);
		setDepth(-16);
		
		sprite = Sprite.get("sToothGhost");
		orientation = Sprite.NORTH;
		
		imageSingle = Calc.random(4);
		imageSpeed = (3.5 + Calc.random(1.75)) / 30.0;
		
		xscale = (Double)Calc.choose(1.0, -1.0);
		yscale = .5;
		
		vspeed = -3 - Calc.random(6);
		
		setColor(Calc.getColorHSV((int)Calc.random(256), 200, 200));
		}
	
	public void step(){
		super.step();
		
		yscale = Calc.approach(yscale, 1.0, 10.0);
		
		if (imageSingle >= 4)
			imageSingle -= 4;
		
		x += Calc.rangedRandom(2);
		y += Calc.rangedRandom(2);
		
		if (y <= -68)
			destroy();
	}
	
	public void render(){
		sprite.render((int)imageSingle + 4, orientation, x + Calc.rangedRandom(.75), y + Calc.rangedRandom(.75), xscale, yscale, 1, 1, colR, colG, colB);
		sprite.render((int)imageSingle, orientation, x + Calc.rangedRandom(.75), y + Calc.rangedRandom(.75), xscale, yscale, 1, 1, "ffffff");
	}


}
