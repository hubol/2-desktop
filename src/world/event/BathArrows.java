package world.event;

import graphics.Sprite;

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
import world.gameplay.Lock;
import main.Calc;
import main.Entity;
import main.Scene;

public class BathArrows extends Entity{
	public int phase;
	public Shake[] s;
	public int[] arrow;
	public double[] scale;
	public Shake c;

	public BathArrows(double x, double y) {
		super(x, y);
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		c = new Shake(.5);
		
		s = new Shake[4];
		for (int i=0; i<4; i++)
			s[i] = new Shake(.4);
		
		scale = new double[4];
		for (int i=0; i<4; i++)
			scale[i] = 1;
		
		arrow = new int[4];
		arrow[0] = 0;
		arrow[1] = 0;
		arrow[2] = 1;
		arrow[3] = 2;
		
		new SpriteLoader("sBathArrows_2","sBathArrowsCragk");
		
		if (Global.event[EV.BATH_ARROWS] == 0){
			new SoundLoader(false, "sFunkyChimeC", "sFunkyChimeASharp", "sFunkyFade", "sSkullQuake");
			phase = 0;
		} 
		else{
			phase = 4;
		}
		
		alarmInitialize(2);
		setDepth(4);
	}
	
	public void step(){
		for (int i=0; i<4; i++)
			scale[i] = Calc.approach(scale[i], 1, 12);
		
		if (phase < 4){
			int check = Global.RIGHT;
			if (arrow[phase] == 1)
				check = Global.JUMP;
			else if (arrow[phase] == 2)
				check = Global.LEFT;
			
			if (IO.checkFrameKey(check)){
				if (phase == 0)
					Global.preventAudioCrash();
				correct();
			}
		}
		
		alarmStep();
	}
	
	public void correct(){
		if (phase < 4){
			Global.squareParticle(x + (phase * 64) + Calc.rangedRandom(4), y + Calc.rangedRandom(4), 3 + (phase % 2), "FFD808", 2 + Calc.random(2));
			Global.squareParticle(x + (phase * 64) + Calc.rangedRandom(4), y + Calc.rangedRandom(4), 4 + ((phase + 1) % 2), "FFD808", 1 + Calc.random(4.25));
			
			scale[phase] = 1.25;
			
			if (phase == 2)
				Sound.play("sFunkyChimeASharp");
			else{
				Audio.get("sFunkyChimeASharp").stop();
				Sound.play("sFunkyChimeC");
			}
			phase += 1;
			
			if (phase == 4){
				alarm[1] = 30;
				Audio.get("sSkullQuake").setLooping(true);
				Sound.play("sSkullQuake");
				Fg.me.shakeTimer = 60;
				alarm[0] = 60;
			}
		}
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			Global.event[EV.BATH_ARROWS] = 1;
			ArrayList<Lock> list = Scene.getEntityList(Lock.class);
			for (int j=0; j<2; j++)
				list.get(j).forcedUnlock();
			
			Audio.get("sSkullQuake").stop();
			Global.unlocked[9] = true;
		}
		else{
			Sound.play("sFunkyFade");
		}
	}
	
	public void render(){
		Sprite.get("sBathArrowsCragk").render(0, Sprite.WEST, x + c.x, y + c.y, 1, 1, 0, 1, 1, 1, 1);
		
		for (int i=0; i<4; i++){
			Sprite.get("sBathArrows").render(0, Sprite.CENTERED, x + ((s[i].x + Calc.rangedRandom(.125)) / 5.5) + (i * 64) + c.x, y + ((s[i].y + Calc.rangedRandom(.125)) / 5.5) + c.y, scale[i] * .5, scale[i] * .5, arrow[i] * 90, 1, 1, 1, 1);
			if (phase > i)
				Sprite.get("sBathArrows").render(1, Sprite.CENTERED, x + ((s[i].x + Calc.rangedRandom(.125)) / 5.5) + (i * 64) + c.x, y + ((s[i].y + Calc.rangedRandom(.125)) / 5.5) + c.y, scale[i] * .5, scale[i] * .5, arrow[i] * 90, 1, 1, 1, 1);
		}
	}

}
