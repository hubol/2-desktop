package world.event;

import audio.Audio;
import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class BagGive extends Entity{
	public Monster mom;
	
	public int phase;
	
	public int spillPhase;
	public double spillImg;
	public double spillAlpha;
	public final Sprite spill = Sprite.get("sBagSpill");
	
	public Shake s;
	
	public double vproach = 40;
	public double milestone = 0;
	public int shake;
	
	public final double wingSteps = 90;

	public BagGive(double x, double y, Monster mom) {
		super(x, y);
		sprite = Sprite.get("sBag");
		orientation = Sprite.CENTERED;
		
		s = new Shake(.2);
		
		spillPhase = -1;
		spillImg = 0;
		spillAlpha = 0;
		
		phase = 0;
		
		alpha = 0;
		vspeed = -2;
		
		shake = 0;
		
		this.mom = mom;
		
		alarmInitialize(7);
		alarm[0] = 30;
		
		Sound.play("sBagIn");
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			phase = 1;
			//TODO sweep sound???
			alarm[1] = 50;
		}
		else if (i == 1){ //shake
			if (shake < 15){
				phase = 2;
				shake += 1;
				Sound.playPitched("sBagShake", .08);
				alarm[1] = 1;
			}
			else{
				x = 320;
				phase = 3;
				alarm[2] = 15;
			}
		}
		else if (i == 2){ //rotate
			phase = 4;
			alarm[3] = 15;
		}
		else if (i == 3){ //spill
			phase = 5;
			spillPhase = 0;
			alarm[4] = (int)wingSteps;
		}
		else if (i == 4){ //end spill
			mom.animate = true;
			spillPhase = 2;
			alarm[5] = 30;
		}
		else if (i == 5){
			alarm[6] = 30;
			mom.alarm[0] = 45;
			Global.event[EV.MONSTER_WINGED] = 1;
			Global.eventItemUse(2);
		}
		else if (i == 6){
			mom.forceStand = false;
		}
	}
	
	public void step(){
		super.step();
		
		if (phase == 0){ //float above player
			x = Player.me.x;
			vspeed = Calc.approach(vspeed, .5, 25);
			if (alpha < 1)
				alpha += .05;
		}
		else if (phase == 1){ //move above monster
			vspeed = 0;
			x = Calc.approach(x, 320, 10);
			y = Calc.approach(y, mom.ystart - 198, vproach);
			vproach = Calc.approach(vproach, 8, 18);
		}
		else if (phase == 2){ //shake
			double m = 1;
			if (shake % 2 == 0)
				m = -1;
			
			x = 320 + (4 * m);
		}
		else if (phase == 5){
			x = 320;
			if (mom.wingAmount < 1.0 && spillPhase == 1){
				mom.wingAmount = Math.min(1.0, mom.wingAmount + (1.0 / (wingSteps - 20)));
				if (mom.wingAmount >= milestone){
					milestone += .05;
					Audio.get("sMonsterGrow").setPitch(1.0 + (mom.wingAmount / 3));
					Sound.play("sMonsterGrow");
					Fg.me.shakeTimer = 5;
				}
			}
		}
		else if (phase == 4){ //rotate to dump on monster
			angle = Calc.approach(angle, 180, 3.2);
		}
		
		//spill animation + sound
		if (spillPhase == 0 && spillAlpha == 0){
			Audio.get("sBagSpill").setGain(.3);
			Audio.fade("sBagSpill", 1.0, .09);
			Audio.get("sBagSpill").setLooping(true);
			Sound.play("sBagSpill");
			mom.alarm[0] = -1;
			mom.eyeImage = 0;
			spillAlpha = 1;
		}
		if (spillPhase == 0){
			spillImg += 7.1 / 30.0;
			if (spillImg >= 4){
				spillPhase = 1;
				mom.alarm[0] = -1;
				mom.blink(true);
			}
		}
		else if (spillPhase == 1){
			spillImg += 6.5 / 30.0;
			if (spillImg >= 7)
				spillImg -= 3;
		}
		else if (spillPhase == 2){
			Audio.fade("sBagSpill", 0, .08);
			spillImg += 7.7 / 30.0;
			if (spillImg >= 11){
				spillImg = 11;
				spillAlpha -= .07;
				if (spillAlpha < 0){
					alpha = 0;
					spillAlpha  = 0;
					mom.eyeImage = 0;
				}
			}
		}
		//end spill
	}
	
	public void render(){
		spill.render(spillImg, Sprite.NORTH, x + s.x, y + s.y + 21, 1, 1, 0, spillAlpha, 1, 1, 1);
		sprite.render(0, orientation, x + s.x, y + s.y, 1, 1, angle, alpha, 1, 1, 1);
	}

}
