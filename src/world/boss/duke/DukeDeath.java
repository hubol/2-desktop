package world.boss.duke;

import java.util.ArrayList;

import audio.Audio;
import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.particles.JumpDreamPart;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class DukeDeath extends Entity{
	public Shake s = new Shake(1);
	public boolean first, last = false;
	
	public DukeDeath(double x, double y, boolean first) {
		super(x, y);
		sprite = Sprite.get("sDukeDead");
		imageSingle = 0;
		imageSpeed = 0;
		
		Fg.me.shakeTimer = 3;
		
		this.first = first;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-4);
		
		setDirSpeed(Calc.random(360), Calc.random(2));
		
		alarmInitialize(1);
		alarm[0] = 7;
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public DukeDeath(double x, double y){
		super(x, y);
		sprite = Sprite.get("sDukeDead");
		imageSingle = 0;
		imageSpeed = 0;
		
		DukeMusic.me.destroy();
		
		Audio.fade("musBoss06", 0, .08);
		Audio.fade("musBoss06FirstForm", 0, .0775);
		
		Sound.play("sDukeDeathScream");
		
		Player.control = false;
		Player.me.cutMove = true;
		Player.me.cutRight = false;
		Player.me.cutToX = Player.me.x + 1;
		
		Fg.me.shakeTimer = 44;
		first = false;
		last = true;
		
		Scene.destroy(Duketangle.class);
		Scene.destroy(DukeSpawn.class);
		
		ArrayList<DukeBalloon> list = Scene.getEntityList(DukeBalloon.class);
		for (int i=0; i<list.size(); i++){
			Sound.playPitched("sBalloonPop");
			list.get(i).pop();
		}
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE + 19);
		
		setDirSpeed(Calc.random(360), Calc.random(2));
		
		alarmInitialize(1);
		alarm[0] = 44;
	}
	
	public void alarmEvent(int i){
		Sound.playPitched("sDukeDie", .1);
		
		Fg.me.shakeTimer = 5;
		
		int amt = 20;
		if (first||last)
			amt += 40;
		
		for (int k=0; k<amt; k++){
			JumpDreamPart j = new JumpDreamPart(x + Calc.rangedRandom(48.8), y + Calc.rangedRandom(32.8), "FAFF58");
			j.setDepth(-3);
			j.size = 2.9 + Calc.random(5);
			j.setDirSpeed(Calc.random(360), Calc.random(6));
		}
		
		if (first){
			Sound.jarBreakPlay();
			Sound.jarBreakPlay();
			
			DukeMusic.me.music(true);
			Fg.me.shakeTimer = 100;
			
			Audio.get("sSkullQuake").setLooping(true);
			Sound.play("sSkullQuake");
			
			new DukeSecond();
		}
		else if (last){
			Sound.jarBreakPlay();
			Sound.jarBreakPlay();
			Fg.me.shakeTimer = 4;
			
			Global.dropLoot(480, 240, 90);
			if (Global.playerHealth == Global.playerMaxHealth)
				Global.dropLoot(480, 240, 110);
			
			Sound.play("sDukePop");
			Sound.play("sPukeEnd");
			
			Global.addTweet("THE #DUKE IS DEAD");
			
			Player.control= true;
			Player.me.cutMove = false;
			
			Global.event[EV.DUKE_DEFEAT] = 1;
			
			Global.refreshIconMap();
			
			new DukeGhost();
		}
		
		destroy();
	}
	
	public void step(){
		super.step();
		x += Calc.rangedRandom(1);
		y += Calc.rangedRandom(1);
		
		setSpeed(getSpeed() * .98);
	}
	
	public void render(){
		sprite.render(0, orientation, x + s.x + Calc.rangedRandom(.2), y + s.y + Calc.rangedRandom(.2), 1, 1, 0, 1, 1, 1, 1);
	}

}
