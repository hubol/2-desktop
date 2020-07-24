package world.event;

import java.util.ArrayList;

import audio.Audio;
import world.control.Global;
import world.control.Sound;
import world.enemy.WaterSparksMask;
import world.particles.WaterSparksParts;
import main.Entity;
import main.Scene;

public class BathSparksControl extends Entity{
	public final double initSeconds = 1;
	public final double tonePitch = .57 / initSeconds;
	public final int setTimer = (int)Math.round(30 * initSeconds);
	//public final String pattern = "0000012000000034";
	public final int[] pattern = {0,0,0,0,0,1,2,0,0,0,0,0,0,0,3,4};
	public int at;
	public int timer;

	public BathSparksControl(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		
		at = 0;
		timer = 5;
	}
	
	public void step(){
		timer -= 1;
		if (timer <= 0)
			play();
	}
	
	public void render(){
		
	}
	
	public void play(){
		if (Audio.soundExists("sSparkSputter"+(at % 2)))
			Sound.playPitched("sSparkSputter"+(at % 2), .05);
		String s = "sSparkTune" + pattern[at];
		if (Audio.soundExists(s)){
			Audio.get(s).setPitch(tonePitch);
			Sound.play(s);
		}
		
		ArrayList<WaterSparksParts> jizz = Scene.getEntityList(WaterSparksParts.class);
		for (int i=0; i<jizz.size(); i++){
			jizz.get(i).hspeed *= 3;
			jizz.get(i).imageSpeed *= 2.2;
		}
		
		ArrayList<BathSparksSpawn> list = Scene.getEntityList(BathSparksSpawn.class);
		for (int i=0; i<list.size(); i++){
			if (list.get(i).id == at % 2)
				new WaterSparksMask(list.get(i).x, list.get(i).y, setTimer);
		}
		
		at += 1;
		if (at > 15)
			at = 0;
		
		timer = setTimer;
	}

}
