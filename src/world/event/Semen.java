package world.event;

import world.control.Global;
import world.control.Sound;
import world.dream.PenisDream;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Semen extends Entity{
	public boolean grabbed, landed;
	public double grabX, grabY, life, mult, addH;
	public int inv, sound;

	public Semen(double x, double y, double dir, int s) {
		super(x, y);
		
		setDepth(-4);
		
		inv = 6;
		sprite = Sprite.get("sSemen");
		mask = Sprite.get("sSemenMask").mask;
		imageSpeed = 0;
		imageSingle = Calc.random(8);
		setCollisionGroup(Global.DEACTIVATEME);
		
		sound = s;
		
		grabbed = false;
		landed = false;
		grabX = 0;
		grabY = 0;
		
		addH = Calc.rangedRandom(.01);
		
		mult = .6;
		
		life = 30 + Calc.random(60);
		
		angle = Calc.random(360);
		xscale = .6 + Calc.random(.6);
		yscale = .6 + Calc.random(.6);
		
		setDirSpeed(dir, 9);
	}
	
	public void step(){
		if (landed || grabbed)
			setSpeed(0);
		else{
			vspeed += .8;
			hspeed += addH;
			super.step();
		}
		
		mult = Calc.approach(mult, 1.0, 10);
		
		if (grabbed){
			x = Player.me.x + grabX;
			y = Player.me.y + grabY;
		}
		
		inv -= 1;
		
		if (inv <= 0){
			if (!landed){
				if (Scene.collision(this, x, y, Global.BLOCK, Global.SOLIDBLOCK) || (Scene.collision(this, x, y, Global.UPBLOCK) && vspeed > 2.5))
					land();
			}
			
			if (!grabbed){
				if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) <= 20){
					mult = 1.4;
					grabbed = true;
					
					PenisDream.me.total += 1;
					
					grabX = x - Player.me.x;
					grabY = y - Player.me.y;
					life += 40;
					Sound.squishPlay();
				}
			}
			else if (Player.me.apart)
				destroy();
		}
		
		life -= 1;
		if (life <= 0 || alpha < 1){
			alpha -= .04;
			if (alpha <= 0)
				destroy();
		}
	}
	
	public void render(){
		sprite.render(imageSingle, Sprite.CENTERED, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4), xscale * mult, yscale * mult, angle, alpha,1,1,1);
	}
	
	public void land(){
		mult = 1.4;
		landed = true;
		x = (xprevious + x) / 2.0;
		y = (yprevious + y) / 2.0;
		
		Sound.splashPlay(sound);
	}

}
