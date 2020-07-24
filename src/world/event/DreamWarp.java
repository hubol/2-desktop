package world.event;

import world.control.Sound;
import audio.Audio;
import main.Calc;
import main.Entity;

public class DreamWarp extends Entity{
	public int between;
	public double intense, pitch;
	
	public static DreamWarp me;

	public DreamWarp(double x, double y, boolean wind) {
		super(x, y);
		between = 10;
		intense = 1.0;
		pitch = 1.0;
		
		me = this;
		
		visible = false;
		
		if (wind){
			Audio.get("sDreamWind").setGain(0);
			Sound.play("sDreamWind");
			Audio.get("sDreamWind").setLooping(true);
			Audio.get("sDreamWind").setPitch(1);
		}
		
		Audio.fade("sDreamWind", .9, .2);
		
		sound();
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void step(){
		between -= 1;
		if (between <= 0)
			sound();
		
		pitch += .01;
		pitch *= 1.02;
		Audio.get("sDreamWind").setPitch(pitch);
	}
	
	public void sound(){
		Audio.get("sDreamWarp").setPitch(intense + Calc.rangedRandom(.17));
		Sound.play("sDreamWarp");
		intense += .07;
		if (between > 4)
			between -= 1;
	}

}
