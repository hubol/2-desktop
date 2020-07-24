package world.menu;

import audio.Audio;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import main.Entity;

public class MenuMusic extends Entity{
	public SoundLoader s;
	public int at;
	public int timer;
	public boolean fuck, fresh;
	public SoundLoader f = null;

	public MenuMusic() {
		super(0, 0);
		s = new SoundLoader("sMenuAmbience");
		at = Math.abs((Global.getRunId() - 1)) % 3;
		Global.menuSong = at;
		fresh = true;
		song();
		fuck = false;
	}
	
	public void song(){
		//Music.changeMusic("musLittleFlower"+at);
		if (f != null)
			f.destroy();
		f = new SoundLoader("musLittleFlower"+at);
		if (!fresh)
			f.playOnceLoaded[0] = true;
		
		fresh = false;
		
		at += 1;
		if (at > 2)
			at = 0;
		
		timer = 2850;
	}
	
	public void step(){
		timer -= 1;
		if (timer <= 0)
			song();
		
		if (s.loaded && !fuck){
			Audio.get("sMenuAmbience").setGain(0);
			Audio.fade("sMenuAmbience", 1, .005);
			Audio.get("sMenuAmbience").setLooping(true);
			Sound.play("sMenuAmbience");
			fuck = true;
		}
	}
	
	public void render(){
		
	}

}
