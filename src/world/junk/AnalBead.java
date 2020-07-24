package world.junk;

import audio.Audio;
import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.particles.JumpDreamPart;
import world.player.Player;
import main.Calc;
import main.Scene;

public class AnalBead extends BasicEnemy{
	private final int beads = 51;
	private final double dist = 512.0 / (beads - 1);
	private Shake[] s = new Shake[beads + 1];
	
	private double[] addYs = new double[beads];
	
	private final double spd = 4;
	private final double timer = 544.0 / spd;
	
	private boolean active = true;

	public AnalBead(double x, double y) {
		super(x, y);
		setDepth(10);
		
		for (int i=0; i<=beads; i++)
			s[i] = new Shake(.3);
		
		for (int i=0; i<beads; i++)
			addYs[i] = 0;
		
		sprite = Global.sBLOCK;
		mask = sprite.mask;
		orientation = Sprite.CENTERED;
		
		alarmInitialize(1);
		alarm[0] = 1 + (int)((400 - y) / 2.0);
	}
	
	public void alarmEvent(int i){
		active = true;
		x = 48;
		y = ystart;
		alarm[0] = (int)timer;
	}
	
	public void step(){
		if (!active){
			x += spd * 2;
			x += x / 128.0;
			if (x > 592)
				x = 592;
			else{
				for (int i=0; i<2; i++){
					JumpDreamPart j = new JumpDreamPart(x - 5 - Calc.random(4), y + Calc.rangedRandom(6), (String)Calc.choose("ff3838", "ffad10"));
					j.size = 4 + Calc.random(6);
				}
			}
			//y = -69;
		}
		else{
			//movement
			x += spd;
			
			//interaction
			playerCollisionDamagesMe();
			if (Scene.collision(this, x, y, Global.PLAYER) && Player.me.y >= y)
				hurtPlayer();
		}
		
		alarmStep();
	}
	
	public void hurtPlayer(){
		if (active){
			Player.hurtPlayer(80);
		}
	}
	
	public void landDamage(){
		if (active){
			Player.invincible = 1;
			Player.me.jump();
			Player.me.vspeed -= .8;
			kill();
		}
	}
	
	public void gunDamage(){
		if (y == 304 && Global.playerHealth == Global.playerMaxHealth && Player.me.x <= x - 32)
			new Mystery(x, y);
		kill();
	}
	
	public void bombDamage(){
		kill();
	}
	
	public void kill(){
		if (active){
			active = false;
			//x = 320;
			//y = -69;
			Sound.play("sLandOnEnemy");
			Audio.get("sAnalBeadBreak").setPitch(1 + ((400 - y) / 192.0));
			Sound.play("sAnalBeadBreak");
			//TODO gfx
		}
	}
	
	public void render(){
		//back
		Sprite s = Sprite.get("sAnalBeads");
		for (int i=0; i<beads; i++){
			double xx = 64 + this.s[i + 1].x + Calc.rangedRandom(.25) + (i * dist);
			addYs[i] = Calc.approach(addYs[i], ((Math.max(-8, -Math.abs(Math.pow(xx - x, 3))  / 996000.0 )) + (Math.max(-8, -Math.abs(Math.pow(xx - (x - 544), 3))  / 996000.0 )) + (Math.max(-8, -Math.abs(Math.pow(xx - (x + 544), 3))  / 996000.0 ))) * 1.6, 2) + 12;
			s.render(2, orientation, xx, ystart + this.s[i + 1].y + Calc.rangedRandom(.25) + addYs[i], .4, .4, 0, 1, 1, 1, 1);
		}
	}
	
	public void drawBead(int img){
		if (x <= 592){
			Sprite s = Sprite.get("sAnalBeads");
			double xx = x + this.s[0].x;
			s.render(img, orientation, xx, y + this.s[0].y, 1.02, 1.02, 0, 1, 1, 1, 1);
		}
	}

}
