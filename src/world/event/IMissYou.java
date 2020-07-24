package world.event;

import audio.Audio;
import main.Calc;
import main.Entity;

public class IMissYou extends Entity{

	public IMissYou(double x, double y) {
		super(x, y);
	}
	
	public void step(){
		if (Audio.soundExists("musCute"))
			Audio.get("musCute").setPitch(Calc.approach(Audio.get("musCute").getPitch(), 1, 60));
	}
	
	public void render(){
		
	}

}
