package world.particles;

import graphics.Sprite;
import world.control.Global;
import main.Calc;
import main.Entity;

public class WaterSparksParts extends Entity{

	public WaterSparksParts(double x, double y) {
		super(x, y);
		this.y += Calc.rangedRandom(3);
		//this.x += 96 - Calc.random(96);
		xscale = Calc.random(.67);
		yscale = xscale;
		angle = Calc.random(360);
		hspeed = Calc.random(9);
		vspeed = Calc.rangedRandom(1);
		imageSpeed = Calc.random(.2);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		x += Calc.rangedRandom(.3);
		y += Calc.rangedRandom(.3);
		vspeed *= 1.01;
		hspeed += .05;
		hspeed *= 1.025;
		double i = imageSingle;
		super.step();
		if ((int)i != (int)imageSingle)
			angle = Calc.random(360);
		if (imageSingle < i || x >= xstart + 96)
			destroy();
	}
	
	public void render(){
		String[] color = new String[2];
		color[0] = "FFFFFF";
		color[1] = Global.bloodOutlineColor;
		if (Calc.random(1) > .5){
			color[0] = Global.bloodOutlineColor;
			color[1] = "FFFFFF";
		}
		
		Sprite.get("sWaterSparksBack").render(imageSingle, orientation, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4), xscale, yscale, angle, 1, color[0]);
		Sprite.get("sWaterSparks").render(imageSingle, orientation, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4), xscale, yscale, angle, 1, color[1]);
	}

}
