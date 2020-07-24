package world.junk;

import java.util.ArrayList;

import audio.Audio;
import world.Fg;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.player.Player;
import main.Entity;
import main.Scene;

public class FruitControl extends Entity{
	public int desired, done;
	public SoundLoader mine;
	public boolean did;
	
	public boolean redux = (Global.room(0,7));
	public String flute = "sFruitFlute", shit = "sFruitShit";
	public boolean prevent = false;

	public FruitControl(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		visible = false;
		
		if (redux){
			flute = "sFruitContra";
			shit = "sFruitAwful";
		}
		mine = new SoundLoader("sFruitCollect", shit, flute);
		did = false;
		
		done = 0;
		
		desired = 0;
		if (Player.me.x < 240)
			desired = 1;
		
		alarmInitialize(1);
	}
	
	public void alarmEvent(int i){
		int a = 30 + desired;
		if (redux)
			a = 50 + desired;
		
		if (Global.event[a] == 0){
			Global.event[a] = 1;
			Audio.fade(flute, 0, .0007);
			if(redux)
				Audio.fade(shit, 0.0, .006);
			Fg.me.shakeTimer = 12;
		}
	}
	
	public void step(){
		if (!did && mine.loaded){
			Audio.get(shit).setLooping(true);
			Audio.get(shit).setGain(0);
			Audio.fade(shit, 0.0, .06);
			Audio.get(flute).setGain(0);
			Audio.fade(flute, .22, .01);
			Audio.get(flute).setPitch(1.0);
			Audio.get(flute).setLooping(true);
			Sound.play(shit);
			Sound.play(flute);
			did = true;
		}
		
		alarmStep();
	}
	
	public void fail(){
		ArrayList<Fruit> fruit = Scene.getEntityList(Fruit.class);
		for(int i=0; i<fruit.size(); i++){
			fruit.get(i).pop();
			fruit.get(i).destroy();
		}
		for(int i=0; i<fruit.size(); i++)
			Global.explosion(fruit.get(i).x, fruit.get(i).y, 0);
		
		if (redux)
			Player.hurtPlayer(300);
		else
			Player.hurtPlayer(150);
		
		Sound.playPitched("sBombBlast",.05);
		Fg.me.shakeTimer = 25;
		
		Audio.fade(flute, 0, .0007);
	}
	
	public void collect(int i){
		if (!prevent){
			Global.preventAudioCrash();
			prevent = true;
		}
		
		if (i == desired){
			Fg.me.shakeTimer = 3;
			
			done += 1;
			if (did){
				Sound.playPitched("sFruitCollect");
				if (!redux)
					Audio.get(flute).setPitch(1.0 + ((double)done / 6.0));
				else
					Audio.get(flute).setPitch(1.0 + ((double)done / 8.0));
				Audio.get(shit).setGain(1);
			}
			
			if (!redux){
				if (done == 6)
					alarm[0] = 5;
			}
			else{
				if (desired == 0)
					desired = 1;
				else
					desired = 0;
				
				if (done == 8)
					alarm[0] = 5;
			}
		}
		else
			fail();
	}

}
