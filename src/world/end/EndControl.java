package world.end;

import java.util.ArrayList;

import audio.Audio;
import audio.SoundClip;

import world.Bg;
import world.Fg;
import world.Overlay;
import world.control.Global;
import world.control.SaveFile;
import world.control.Sound;
import world.gameplay.Bed;

import main.Calc;
import main.Entity;
import main.Scene;

public class EndControl extends Entity{
	private ArrayList<EndFlower> events = new ArrayList<EndFlower>(), hearts = new ArrayList<EndFlower>();
	private final double max = 79;
	
	private double current = 0;
	private int heart = 0, event = 0, snd = 0;
	
	private boolean goPitch = false;
	private double pitch = 0;
	
	private SoundClip tune, drum, brass;
	
	private final double WAIT = 7.8260869565217391304347826086957 / 2.0;
	private double POOL = 0;
	
	private final long endFrames = Global.framesPlayed + Global.framesPaused, endFramesPlayed = Global.framesPlayed, endFramesPaused = Global.framesPaused;
	
	public EndControl() {
		super(0, 0);
		
		SaveFile.saveHearts();
		SaveFile.saveStats();
		SaveFile.tagWrite(Global.saveFileNumber - 1, "FINAL - " + Global.getPercentage());
		
		tune = Audio.get("sEndTune");
		drum = Audio.get("sEndDrum");
		brass = Audio.get("sEndBrass");
		tune.setLooping(true);
		drum.setLooping(true);
		brass.setLooping(true);
		tune.setPitch(1);
		drum.setPitch(1);
		brass.setPitch(1);
		
		tune.play();
		drum.play();
		brass.play();
		
		tune.setGain(.2);
		Audio.fade("sEndTune", .6, .0005);
		drum.setGain(0);
		Audio.fade("sEndDrum", 0, 1.0);
		brass.setGain(0);
		Audio.fade("sEndBrass", 0, 1.0);
		
		ArrayList<EndFlower> temp = Scene.getEntityList(EndFlower.class);
		for (int i=0; i<temp.size(); i++){
			EndFlower t = temp.get(i);
			if (t.isHeart)
				hearts.add(t);
			else
				events.add(t);
		}
		
		visible = false;
		
		alarmInitialize(6);
		alarmEvent(0);
	}
	
	public boolean grow(EndFlower f){
		boolean g = f.grow();
		if (g){
			drum.setGain(1 + Math.min(1, (current / max) * 1.25) * .8);
			Sound.playPitched("sEndGrow"+snd, .125);
			if (snd == 0)
				snd = 1;
			else
				snd = 0;
			current++;
		}
		return g;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			if (heart < 72){
				grow(hearts.get(heart));
				if (heart % 9 == 5 && event < 7){
					grow(events.get(event));
					event += 1;
				}
			}
			heart += 1;
			if (heart < 72){
				POOL += WAIT;
				double REMAINDER = POOL % 1;
				alarm[0] = (int)(POOL - REMAINDER);
				POOL = REMAINDER;
			}
			else if (heart == 129)
				alarmEvent(1);
			else{
				POOL += WAIT;
				double REMAINDER = POOL % 1;
				alarm[0] = (int)(POOL - REMAINDER);
				POOL = REMAINDER;
			}
		}
		else if (i == 1){
			goPitch = true;
			Audio.fade("sEndDrum", 0, .00225);
			Audio.fade("sEndTune", 0, .0025);
			Audio.fade("sEndBrass", 0, .0015);
			
			Fg.me.shakeTimer = 6;
			Sound.play("sEndBroken");
			Scene.getEntityList(Bed.class).get(0).endingExplosion();
			Fg.me.fade = true;
			Bg.me.fade = true;
			Overlay.me.fade = true;
			ArrayList<EndFlower> list = Scene.getEntityList(EndFlower.class);
			for (int j=0; j<list.size(); j++)
				list.get(j).fade = true;
			alarm[2] = 500;
		}
		else if (i == 2){
			new EndIndicator(endFrames, endFramesPlayed, endFramesPaused);
		}
	}
	
	public void step(){
		//final double pitch = 1.0 + Math.max(0, Math.pow(current / max, 2) * .25);
		//drum.setPitch(pitch);
		//tune.setPitch(pitch);
		
		
		if (goPitch){
			pitch = Calc.approach(pitch, 2, 40);
			drum.setPitch(pitch);
			tune.setPitch(pitch);
			brass.setPitch(pitch);
		}
		else{
			Audio.fade("sEndDrum", Math.min(1, (current / max) * 1.25) * .8, .07);
			Audio.fade("sEndBrass", Math.max(0, (current / max) - .9) * 10.0, .07);
		}
		
		alarmStep();
	}
	
	public void render(){
		
	}

}
