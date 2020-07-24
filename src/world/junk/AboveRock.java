package world.junk;

import java.util.ArrayList;

import graphics.Sprite;
import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.dream.DreamHeartParticles;
import world.gameplay.Upblock;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class AboveRock extends Entity{
	public Shake s = new Shake(.4);
	public Upblock mine;
	
	public boolean above;
	public int entry;
	
	public boolean fall, a = false, noSound = false;

	public AboveRock(double x, double y) {
		super(x, y);
		new SpriteLoader("sAboveRock_5");
		sprite = Sprite.get("sAboveRock");
		orientation = Sprite.SOUTH;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		mine = new Upblock(x - 16, y - 32);
		
		above = Global.room(8, 6);
		entry = Global.event[EV.LOVERCRACK];
		
		if (above){
			if (entry == 0)
				new SoundLoader("sRockScream", "sRockLoverLand");
			else{
				mine.destroy();
				destroy();
			}
		}
		else if (entry == 0){
			mine.destroy();
			destroy();
		}
		
		if (entry == 0)
			imageSingle = 0;
		else
			imageSingle = 2;
		
		alarmInitialize(3);
		alarm[0] = 1 + (int)Calc.random(90);
		
		setDepth(4);
		
		if (!above && entry == 1){
			LovelessRock r = Scene.getEntityList(LovelessRock.class).get(0);
			r.noSound = true;
			noSound = true;
			
			//simulate 180 steps
			for (int i=0; i<180; i++){
				r.step();
				step();
				ArrayList<DreamHeartParticles> fuckyou = Scene.getEntityList(DreamHeartParticles.class);
				for (int j=0; j<fuckyou.size(); j++)
					fuckyou.get(j).step();
			}
			
			r.noSound = false;
			noSound = false;
		}
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			if (!fall){
				if (!noSound)
					Sound.playPitched("sRockBlink");
				imageSingle += 1;
			}
			alarm[1] = 3 + (int)Calc.random(3);
		}
		else if (i == 1){
			if (!fall)
				imageSingle -= 1;
			alarm[0] = 1 + (int)Calc.random(90);
		}
		else if (i == 2){
			Player.control = true;
			Player.me.stepActive = true;
			
			Sound.play("sRockLoverLand");
			Fg.me.shakeTimer = 9;
			destroy();
		}
	}
	
	public void step(){
		a = !a;
		
		if (!above){
			if (Global.event[EV.LOVERCRACK] == 1 && a)
				new DreamHeartParticles(x + Calc.rangedRandom(2), y - 36 - Calc.random(3.5), 90 + Calc.rangedRandom(7), 3);
		}
		else{
			if (Global.event[EV.LOVERCRACK] != entry){
				if (!fall){
					mine.destroy();
					fall = true;
					imageSingle = 4;
					Sound.play("sRockScream");
					orientation = Sprite.CENTERED;
					y -= 24;
					vspeed = -12;
					setDepth(Integer.MIN_VALUE + 10);
					
					Player.control = false;
					Player.me.stepActive = false;
					
					alarm[2] = 45; //TODO adjust
				}
				if (fall){
					vspeed += 1;
					y += vspeed;
					
					angle += 8 + Math.abs(vspeed / 12.0);
				}
			}
		}
		alarmStep();
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + s.x + Calc.rangedRandom(.2), y + s.y + Calc.rangedRandom(.2), .85, .85, angle, 1, 1, 1, 1);
	}

}
