package world.event;

import world.control.Global;
import world.control.Sound;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class SunlightBottomDrop extends Entity{
	public double mult, aprch, add, a, b;
	public int last;
	public boolean play, splash, rm;

	public SunlightBottomDrop(double x, double y) {
		super(x, y);
		
		splash = false;
		rm = Global.room(6, 6);
		
		play = true;
		
		mult = 0;
		aprch = Calc.random(8, 12) * .5;
		
		last = 0;
		
		a = Calc.random(.3, .5);
		b = Calc.random(.6, .8);
		
		add = Calc.random(.32);
		
		xscale = Calc.random(.4, .62) * (Double)Calc.choose(1.0, -1.0);
		yscale = Math.abs(xscale);
		imageSingle = Calc.random(1.5);
		imageSpeed = Calc.random(.3);
		
		sprite = Sprite.get("sSunlightBottomDrip");
		orientation = Sprite.NORTH;
		
		mask = Sprite.get("cum").mask;
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		if (!splash){
			mult = Calc.approach(mult, 1, aprch);
			
			imageSpeed *= 1.03;
			imageSingle += imageSpeed;
			
			if ((int)imageSingle != last){
				y += add;
				last = (int)imageSingle;
			}
			
			if (imageSingle >= 10){
				imageSingle = 10;
				imageSpeed = 0;
				if (vspeed == 0){
					if (play && !rm)
						Sound.dropsPlay();
					vspeed = a;
				}
				vspeed += b;
				
				vspeed *= 1.00175;
				
				x += Calc.rangedRandom(1);
				y += Calc.rangedRandom(1);
				
				if (y >= 500)
					destroy();
			}
			
			if (!rm)
				y += vspeed;
			else{
				int i = 0;
				while (!Scene.collision(this, x, y+1, Global.SOLIDBLOCK) && i < vspeed){
					y += 1;
					i += 1;
				}
				if (Scene.collision(this, x, y + 1, Global.SOLIDBLOCK)){
					vspeed = 0;
					sprite = Sprite.get("sStupidSplash");
					imageSingle = 0;
					imageSpeed = (8 + Calc.random(5)) / 30.0;
					
					if (play)
						Sound.playPitched("sStupidSplash", .125);
					
					splash = true;
				}
			}
		}
		else{
			xscale *= 1.02;
			
			imageSingle += imageSpeed;
			imageSpeed += .16;
			imageSpeed *= 1.125;
			if (imageSingle >= 6)
				destroy();
		}
	}
	
	public void render(){
		sprite.render(imageSingle,orientation,x + Calc.rangedRandom(.7), y + Calc.rangedRandom(.7), xscale * mult, yscale * mult, 0, 1, 1, 1, 1);
	}

}
