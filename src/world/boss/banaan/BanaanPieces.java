package world.boss.banaan;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class BanaanPieces extends Entity{
	public double iX, iY, h;

	public BanaanPieces(double x, double y, double iX, double iY, int img) {
		super(x, y);
		this.iX = iX;
		this.iY = iY;

		setDepth(Integer.MIN_VALUE+10);
		setCollisionGroup(Global.DEACTIVATEME);

		sprite = Sprite.get("sBanaan");
		
		imageSingle = img;
		imageSpeed = 0;
		
		hspeed = Calc.rangedRandom(8);
		h = Calc.random(Math.signum(hspeed * -1));
		vspeed = -Calc.random(9);
	}
	
	public void step(){
		hspeed = Calc.approach(hspeed, h, 15);
		vspeed += .78;
		vspeed *= 1.001;
		
		x += Calc.rangedRandom(.3);
		y += Calc.rangedRandom(.3);
		
		super.step();
		
		if (y > 500)
			destroy();
	}
	
	public void render(){
		sprite.renderPart((int)imageSingle, Sprite.NORTHWEST, x, y, iX, iY, 2, 2, 1, 1, 0, 1, 1, 1, 1);
	}

}
