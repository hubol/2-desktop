package world.junk;

import java.util.ArrayList;

import audio.Audio;

import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.IO;
import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.gameplay.Balloon;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class FinalLever extends Entity{
	public boolean can, action;
	public Shake s = new Shake(.4), t = new Shake(.4);
	public double stangle;
	public int shabip;
	
	public final double fuck = 50;
	
	public int enabled = 0;
	public int seconds = 0, life;
	
	public boolean go = false, shit = false;
	
	public int EVENT, SECS;

	public FinalLever(double x, double y) {
		super(x, y);
		new SpriteLoader("sFinalLever_2");
		
		sprite = Sprite.get("sFinalLever");
		imageSpeed = 0;
		orientation = Sprite.SOUTH;
		
		shabip = 0;
		
		angle = -fuck + ((fuck * 2) * enabled);
		can = true;
		action = false;
		
		setDepth(-5);
		setCollisionGroup(Global.INTERACTABLE, Global.DEACTIVATEME);

		new SoundLoader(false, "sRainLeverInitClick", "sRainLeverEndClick", "sFinalLeverChime0", "sFinalLeverChime1", "sDishError", "sDreamHeartAppear", "sFlowerTick");
		Global.preventAudioCrash();
		
		alarmInitialize(1);
		
		EVENT = EV.FINALBALLOON;
		SECS = 16;
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
					generateBalloons();
					
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
					
					popBalloons();
				}
				
				Player.control = true;
				Sound.playPitched("sRainLeverInitClick",.05);
				
				Audio.get("sFinalLeverChime0").stop();
				Audio.get("sFinalLeverChime1").stop();
				
				Sound.playPitched("sFinalLeverChime"+enabled, .05);
				can = true;
			}
		}
		
		if (Global.playerHealth < life && go)
			fail();
		
		alarmStep();
		
		if (go && !Scene.instanceExists(Balloon.class))
			Global.event[EVENT] = 1;
	}
	
	public void generateBalloons(){
		ArrayList<Marker> markers = Scene.getEntityList(Marker.class);
		for (int l=0; l<markers.size(); l++){
			Marker m = markers.get(l);
			for (int i=0; i<4; i++)
				Global.squareParticle(m.x + Calc.rangedRandom(6), m.y + Calc.rangedRandom(6), 3 + (int)Calc.random(5), Global.roomColor, 1.5 + Calc.random(5));
			
			new Balloon(m.x, m.y);
		}
	}
	
	public void popBalloons(){
		ArrayList<Balloon> balloons = Scene.getEntityList(Balloon.class);
		for (int i=0; i<balloons.size(); i++)
			balloons.get(i).pop();
	}
	
	public void render(){
		sprite.render(0, orientation, x + s.x, y + s.y, 1, 1.2, angle, 1, 1, 1, 1);
		sprite.render(1, orientation, x + t.x, y + t.y, 1.2, 1.2, 0, 1, 1, 1, 1);
	}

}
