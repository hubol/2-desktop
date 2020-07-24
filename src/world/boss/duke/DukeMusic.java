package world.boss.duke;

import world.control.Sound;
import audio.Audio;
import main.Entity;

public class DukeMusic extends Entity{
	public static DukeMusic me;
	public boolean phase;

	public DukeMusic() {
		super(0, 0);
		me = this;
		
		Audio.get("musBoss06").setGain(0);
		Audio.get("musBoss06FirstForm").setGain(1);
		Audio.get("musBoss06").setLooping(true);
		Audio.get("musBoss06FirstForm").setLooping(true);
		Audio.fade("musBoss06", 0, 1.0);
		Audio.fade("musBoss06FirstForm", 1, 1.0);
		
		Sound.play("musBoss06");
		Sound.play("musBoss06FirstForm");
	}
	
	public void music(boolean phase){
		this.phase = phase;
		if (phase){
			Audio.fade("musBoss06", 1.0, .01);
			Audio.fade("musBoss06FirstForm", 0.0, .01);
		}
		else{
			Audio.fade("musBoss06FirstForm", 1.0, .01);
			Audio.fade("musBoss06", 0.0, .01);
		}
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}

}
