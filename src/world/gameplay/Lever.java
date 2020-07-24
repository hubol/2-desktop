package world.gameplay;

import java.util.ArrayList;

import audio.Audio;

import world.control.EV;
import world.control.Global;
import world.control.IO;
import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import world.event.NewRain;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Lever extends Entity{
	public boolean can, action;
	public Shake s = new Shake(.4), t = new Shake(.4);
	public double stangle;
	public int shabip;
	
	public final double fuck = 50;
	public LeverDraw l;

	public Lever(double x, double y) {
		super(x, y);
		sprite = Sprite.get("sRainLever");
		imageSpeed = 0;
		orientation = Sprite.SOUTH;
		
		shabip = 0;
		
		angle = -fuck + ((fuck * 2) * Global.event[EV.RAINBLOCK]);
		can = true;
		action = false;
		
		setDepth(5);
		setCollisionGroup(Global.INTERACTABLE, Global.DEACTIVATEME);
		
		l = new LeverDraw(x, y, this);
		
		if (Scene.getEntityList(Lever.class).size() == 1){
			new SoundLoader(false, "sRainLeverInitClick", "sRainLeverEndClick", "sLeverChime0", "sLeverChime1");
			Global.preventAudioCrash();
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
				
				if (Global.event[EV.RAINBLOCK] == 1)
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
				if (Global.event[EV.RAINBLOCK] == 1)
					Global.event[EV.RAINBLOCK] = 0;
				else
					Global.event[EV.RAINBLOCK] = 1;
				
				ArrayList<LeverBlock> list = Scene.getEntityList(LeverBlock.class);
				for (int i=0; i<list.size(); i++)
					list.get(i).change();
				
				ArrayList<NewRain> bist = Scene.getEntityList(NewRain.class);
				for (int i=0; i<bist.size(); i++)
					bist.get(i).refresh();
				
				Player.control = true;
				Sound.playPitched("sRainLeverInitClick",.05);
				
				Audio.get("sLeverChime0").stop();
				Audio.get("sLeverChime1").stop();
				
				Sound.playPitched("sLeverChime"+Global.event[EV.RAINBLOCK], .05);
				can = true;
			}
		}
	}
	
	public void draw(){
		sprite.render(0, orientation, x + s.x, y + s.y, 1, 1.2, angle, 1, 1, 1, 1);
		sprite.render(1, orientation, x + t.x, y + t.y, 1.2, 1.2, 0, 1, 1, 1, 1);
	}
	
	public void render(){

	}

}
