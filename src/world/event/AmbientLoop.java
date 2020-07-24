package world.event;

import audio.Audio;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import main.Entity;

public class AmbientLoop extends Entity{
	public String loop;
	public SoundLoader myFriend;
	public boolean played;

	public AmbientLoop(double x, double y, String s) {
		super(0, 0);
		loop = s;
		myFriend = new SoundLoader(s);
		played = false;
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		if (myFriend.loaded && Audio.soundExists(loop)){
			if (!played){
				Audio.get(loop).setLooping(true);
				Audio.get(loop).setGain(0);
				Sound.play(loop);
				played = true;
			}
			
			Audio.fade(loop, 1.0, .05);
		}
	}
	
	public void render(){
		if (!stepActive && Audio.soundExists(loop))
			Audio.fade(loop, .375, .09);
	}

}
