package world.menu;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class MenuFlower extends Entity{
	public Shake s;
	public double at, angSpd, ang;
	
	public double toXsc, toYsc, xAprch, yAprch, baseAng;
	

	public MenuFlower(double x, double y, double xsc, double ysc, double ang, double angSpd, double init, int img, double shakeSpd, double xAprch, double yAprch, double base, int d) {
		super(x, y);
		s = new Shake(shakeSpd);
		xscale = 0;
		yscale = 0;
		
		this.xAprch = xAprch;
		this.yAprch = yAprch;
		
		toXsc = xsc;
		toYsc = ysc;
		
		this.ang = ang;
		at = init;
		this.angSpd = angSpd;
		
		sprite = Sprite.get("sMenuFlowersLine");
		orientation = Sprite.SOUTH;
		
		imageSpeed = 0;
		imageSingle = img;
		
		baseAng = base;
		setDepth(60 + d);
	}
	
	public void step(){
		at += angSpd;
		
		xscale = Calc.approach(xscale, toXsc, xAprch);
		yscale = Calc.approach(yscale, toYsc, yAprch);
		
		while (at >= 60)
			at -= 60;
		angle = baseAng + (Math.sin((at / 30.0) * Math.PI) * ang);
	}
	
	public void render(){
		double x = this.x + s.x + Calc.rangedRandom(.25), y = this.y + s.y + Calc.rangedRandom(.25);
		Sprite.get("sMenuFlowersLine").render(imageSingle, orientation, x, y, xscale, yscale, angle, 1, Global.menuBackgroundColor);
		Sprite.get("sMenuFlowersBack").render(imageSingle, orientation, x, y, xscale, yscale, angle, 1, Global.menuLineColor);
	}

}
