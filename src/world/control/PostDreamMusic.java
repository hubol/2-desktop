package world.control;

import audio.Audio;
import main.Calc;
import main.Entity;
import main.Scene;

public class PostDreamMusic extends Entity{
	public double pitch;
	public String music;
	public boolean yes;

	public PostDreamMusic(String mus) {
		super(0, 0);
		persistent = true;
		pitch = 8096.0 + Calc.random(8096.0);
		music = mus;
		
		yes = false;
		
		set();
	}
	
	public void roomDestroy(){
		
	}
	
	public void set(){
		if (Audio.soundExists(music)){
			Audio.get(music).setPitch(pitch);
			if (Scene.console)
				System.out.println("pitch!!!!!!");
		}
		else
			destroy();
	}
	
	public void step(){
		if (yes){
			pitch = Calc.approach(pitch, 1, 1.2);
			pitch *= .3;
			pitch -= 32;
			if (pitch <= 1){
				pitch = 1;
				set();
				destroy();
			}
			else
				set();
		}
		else
			yes = true;
	}
	
	public void render(){
		
	}

}
