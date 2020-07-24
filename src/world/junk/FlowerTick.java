package world.junk;

import audio.Audio;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.player.Player;
import main.Entity;

public class FlowerTick extends Entity{
	public SoundLoader s;

	public FlowerTick(double x, double y) {
		super(x, y);
		s = new SoundLoader("sFlowerTick");
		
		alarmInitialize(1);
		alarm[0] = 15;
		
		visible = false;
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void alarmEvent(int i){
		Sound.playPitched("sFlowerTick", .1);
		alarm[0] = 15;
	}
	
	public void step(){
		if (s.loaded){
			double s = 1.0 - ((Math.min(Player.me.y, 380.0) / 380.0));
			Audio.get("sFlowerTick").setGain(s);
			Audio.fade("sFlowerTick", s, s);
			alarmStep();
		}
	}

}
