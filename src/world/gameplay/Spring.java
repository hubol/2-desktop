package world.gameplay;

import audio.Audio;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import world.Fg;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.particles.Sparkle;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class Spring extends Entity{
	public double spd;
	public int amount;
	
	public double mult = 1;
	
	public boolean special = false;
	public double xs = .5, ys = .45;

	public Shake s = new Shake(.2), t = new Shake(.2);
	//5.31 gets up 1 block, 7.41 gets up 2 blocks, 9.1 gets up 3, 10.5 up 4, 11.8 up 5, 12.9 up 6
	
	public final static Sprite SPRING = Sprite.get("sSpring");

	public Spring(double x, double y, int power) {
		super(x, y);
		
		final double[] spds = new double[]{5.31, 7.41, 9.1, 10.5, 11.8, 12.9, 13.9};
		spd = spds[power - 1];
		amount = power + 1;
		
		orientation = Sprite.SOUTH;
		
		mask = Global.sUPBLOCK.mask;
		sprite = SPRING;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(1);
	}
	
	public Spring(double x, double y, int power, boolean fuck) {
		super(x, y);
		
		final double[] spds = new double[]{5.31, 7.41, 9.1, 10.5, 11.8, 12.9};
		spd = spds[power - 1];
		amount = power + 1;
		
		orientation = Sprite.SOUTH;
		
		xs = .7;
		ys = .6;
		special = true;
		mask = Global.sUPBLOCK.mask;
		sprite = Sprite.get("sStupidSpring");
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(1);
		
		alarmInitialize(1);
		alarm[0] = 20;
	}
	
	public void alarmEvent(int i){
		Sparkle s = new Sparkle(x + Calc.rangedRandom(22), y - Calc.random(22));
		s.setDepth(Integer.MIN_VALUE + 4);
		alarm[0] = 1 + (int)Calc.random(10);
	}
	
	public void step(){
		if (Scene.collision(this, x, y - 1, Global.PLAYER) && Player.me.vspeed >= 0 && vspeed == 0){
			Audio.get("sSpringNote").setPitch(1.0 + ((x - 16) / 320.0));
			Sound.play("sSpringNote");
			Sound.playPitched("sSpring", .06);
			Player.me.y = y - 32;
			Player.me.jump();
			Player.me.vspeed = -spd;
			Player.me.y += Player.me.vspeed;
			vspeed = -spd;
			
			if (!Global.room(3, 5))
				Fg.me.shakeTimer = 6;
			else if (Fg.me.shakeTimer <= 0)
				Fg.me.shakeTimer = 6;
			
			if (Global.gotWallSlide){
				Player.me.record = true;
				Player.me.cancel = false;
				Player.me.cameOffWallJump = true;
			}
		}
		
		super.step();
		if (y < ystart)
			vspeed += 1.3;
		if (y >= ystart){
			y = ystart;
			vspeed = 0;
		}
		
		if (vspeed < 0)
			mult = Calc.approach(mult, 1.2, 3);
		else
			mult = Calc.approach(mult, 1, 3.5);
	}
	
	public void render(){
		Graphics.setColor("0081CF");
		double m = (ystart - y + 5) / (double)amount;
		double f = 0;
		for (int i=0; i<amount; i++){
			Shape.drawLine(x + 13 + Calc.rangedRandom(.05) + t.x, y - 2 + (i * m) + Calc.rangedRandom(.05) + t.y + f, x - 13 + Calc.rangedRandom(.05) + t.x, y - 2 + ((i + 1) * m) + Calc.rangedRandom(.05) + t.y + f, 1.95 + Calc.random(.35));
			Shape.drawLine(x - 13 + Calc.rangedRandom(.05) - t.y, y - 2 + (i * m) + Calc.rangedRandom(.05) - t.x + f, x + 13 + Calc.rangedRandom(.05) - t.y, y - 2 + ((i + 1) * m) + Calc.rangedRandom(.05) - t.x + f, 1.95 + Calc.random(.35));
		}
		/*Shape.drawLine(x + t.x + 13 + Calc.rangedRandom(.05), y + t.y - 2 + Calc.rangedRandom(.05), x + t.x - 13 + Calc.rangedRandom(.05), ystart + 4 + t.y + Calc.rangedRandom(.05), 1.8 + Calc.random(.2));
		Shape.drawLine(x - t.y - 13 + Calc.rangedRandom(.05), y - t.x - 2 + Calc.rangedRandom(.05), x - t.y + 13 + Calc.rangedRandom(.05), ystart + 4 - t.x + Calc.rangedRandom(.05), 1.8 + Calc.random(.2));*/
		
		//sprite.render(0, orientation, x + s.x + Calc.rangedRandom(.05), y + s.y + Calc.rangedRandom(.05), .5 * mult, .45, 0, 1, 1, 1, 1);
	}
	
	public void fuck(){
		sprite.render(0, orientation, x + s.x + Calc.rangedRandom(.05), y + s.y + Calc.rangedRandom(.05), xs * mult, ys, 0, 1, 1, 1, 1);
	}

}
