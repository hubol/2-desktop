package world.boss.puke;

import world.control.Sound;
import audio.Audio;
import main.Entity;

public class PukeMusic extends Entity{
	public static PukeMusic me;
	public boolean phase;

	public PukeMusic() {
		super(0, 0);
		me = this;
		
		Audio.get("musBoss05").setGain(0);
		Audio.get("musBoss05FirstForm").setGain(1);
		Audio.get("musBoss05").setLooping(true);
		Audio.get("musBoss05FirstForm").setLooping(true);
		Audio.fade("musBoss05", 0, 1.0);
		Audio.fade("musBoss05FirstForm", 1, 1.0);
		
		Sound.play("musBoss05");
		Sound.play("musBoss05FirstForm");
	}
	
	public void music(boolean phase){
		this.phase = phase;
		if (phase){
			Audio.fade("musBoss05", 1.0, .01);
			Audio.fade("musBoss05FirstForm", 0.0, .01);
		}
		else{
			Audio.fade("musBoss05FirstForm", 1.0, .01);
			Audio.fade("musBoss05", 0.0, .01);
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
