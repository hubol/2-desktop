package world.junk;

import java.util.ArrayList;

import audio.Audio;
import world.Fg;
import world.control.Global;
import world.control.IO;
import world.control.MapIconInfluence;
import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.dream.DreamHeartParticles;
import world.gameplay.Heart;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class PlantLever extends MapIconInfluence{
	public boolean can, action;
	public Shake s = new Shake(.4), t = new Shake(.4);
	public double stangle;
	public int shabip;
	
	public final double fuck = 50;
	
	public int enabled = 0;
	public int seconds = 0, life;
	
	public boolean go = false, shit = false;
	
	public int HEART, SECS, HEARTX, HEARTY;

	public PlantLever(double x, double y) {
		super(x, y);
		init();
		
		if (Global.room(7, 6)){
			HEART = 46;
			SECS = 12;
			HEARTX = 304;
			HEARTY = 38;
		}
		
		ArrayList<Heart> f = Scene.getEntityList(Heart.class);

		if (f.size() > 0)
			f.get(0).destroy();
	}
	
	public PlantLever(double x, double y, int heart, int secs, int heartx, int hearty){
		super(x, y);
		init();
		
		HEART = heart;
		SECS = secs;
		HEARTX = heartx;
		HEARTY = hearty;
		
		ArrayList<Heart> f = Scene.getEntityList(Heart.class);

		if (f.size() > 0)
			f.get(0).destroy();
	}
	
	public void refreshIcon(){
		if (!Global.heartGot[HEART])
			Global.setIconMap(Global.roomX, Global.roomY, 4);
	}
	
	public void init(){
		new SpriteLoader("sPlantLever_2");
		
		sprite = Sprite.get("sPlantLever");
		imageSpeed = 0;
		orientation = Sprite.SOUTH;
		
		shabip = 0;
		
		angle = -fuck + ((fuck * 2) * enabled);
		can = true;
		action = false;
		
		setDepth(-5);
		setCollisionGroup(Global.INTERACTABLE, Global.DEACTIVATEME);

		new SoundLoader(false, "sRainLeverInitClick", "sRainLeverEndClick", "sPlantLeverChime0", "sPlantLeverChime1", "sDishError", "sDreamHeartAppear", "sFlowerTick");
		Global.preventAudioCrash();
		
		alarmInitialize(1);
	}
	
	public void alarmEvent(int i){
		seconds -= 1;
		if (seconds <= 0)
			fail();
		else{
			Fg.me.shakeTimer = 3;
			Sound.playPitched("sFlowerTick", .05);
			alarm[0] = 15;
		}
		
		shit = !shit;
	}
	
	public void fail(){
		Sound.playPitched("sDishError");
		Fg.me.shakeTimer = 15;
		can = false;
		enabled = 1;
		
		go = false;
		
		alarm[0] = -1;
		seconds = 0;
		
		stangle = angle;
		
		ArrayList<Heart> f = Scene.getEntityList(Heart.class);

		if (f.size() > 0)
			f.get(0).destroy();
		
		heartParticles();
	}
	
	public void heartParticles(){
		double x = HEARTX, y = HEARTY; //awful. shit awful
		if (!Global.heartGot[HEART]){
			for (int i=0; i<12; i++){
				new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
				DreamHeartParticles j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
				j.setSpeed(j.getSpeed() * 4);
				if (i > 3){
					j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
					j.setSpeed(j.getSpeed() * 8);
				}
			}
		}
	}
	
	public void step(){
		action = false;
		if (can){
			if (Scene.collision(this,x,y,Global.PLAYER) && Player.me.vspeed == 0 && Player.me.y == y - 16 && Player.downTime <= 0 && !Global.paused && Player.me.hspeed == 0){
				action=true;
				Player.canChangeWeapon = false;
				Global.drawDown(x, y - 38);
			}
			
			if (action && Player.canDownInteract() && IO.checkFrameKey(Global.DOWN)){
				can = false;
				stangle = angle;
				
				if (enabled == 1)
					shabip = 0;
				else
					shabip = 1;
				
				Player.control = false;
				Sound.playPitched("sRainLeverEndClick", .05);
			}
		}
		else{
			angle = Calc.approach(angle, -fuck + ((fuck * 2) * shabip), 10);
			angle -= 10 * Math.signum(stangle);
			if (Math.signum(stangle) != Math.signum(angle))
				angle -= 9 * Math.signum(stangle);
			boolean auf = false;
			if (angle < -fuck){
				angle = -fuck;
				auf = true;
			}
			else if (angle > fuck){
				angle = fuck;
				auf = true;
			}
			
			if (auf){
				if (enabled == 1)
					enabled = 0;
				else
					enabled = 1;
				
				if (enabled == 1){
					if (!Global.heartGot[HEART]){
						new Heart(HEARTX, HEARTY, HEART);
						Sound.playPitched("sDreamHeartAppear", .05);
						
						heartParticles();
					}
					
					seconds = SECS;
					life = Global.playerHealth;
					
					shit = false;
					alarmEvent(0);
					shit = false;
					
					go = true;
				}
				else{
					go = false;
					
					alarm[0] = -1;
					seconds = 0;
					
					if (!Global.heartGot[HEART]){
						ArrayList<Heart> f = Scene.getEntityList(Heart.class);
	
						if (f.size() > 0){
							f.get(0).destroy();
						
							heartParticles();
						}
					}
				}
				
				Player.control = true;
				Sound.playPitched("sRainLeverInitClick",.05);
				
				Audio.get("sPlantLeverChime0").stop();
				Audio.get("sPlantLeverChime1").stop();
				
				Sound.playPitched("sPlantLeverChime"+enabled, .05);
				can = true;
			}
		}
		
		if (Global.playerHealth < life && go)
			fail();
		
		alarmStep();
	}
	
	public void render(){
		sprite.render(0, orientation, x + s.x, y + s.y, 1, 1.2, angle, 1, 1, 1, 1);
		sprite.render(1, orientation, x + t.x, y + t.y, 1.2, 1.2, 0, 1, 1, 1, 1);
	}

}
