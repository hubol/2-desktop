package world.event;

import audio.Audio;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import main.Entity;
import main.Scene;

public class HandsomePrince extends Entity{
	public SoundLoader s;
	public boolean done, play;
	public static HandsomePrince me;

	public HandsomePrince(double x, double y) {
		super(x, y);
		persistent = true;
		setCollisionGroup(Global.DEACTIVATEME, Global.KILLATLEAVE);

		s = new SoundLoader("musHandsomePrince", "musHandsomePrinceIntro");
		s.persistent = true;
		done = false;
		visible = false;
		play = false;
		
		alarmInitialize(1);
		
		me = this;
	}
	
	public void destroy(){
		me = null;
		s.destroy();
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void alarmEvent(int i){
		destroy();
	}
	
	public void step(){
		if (s.loaded && !done && Audio.soundExists("musHandsomePrince") && Audio.soundExists("musHandsomePrinceIntro")){
			done = true;
			Audio.get("musHandsomePrince").setLooping(true);
			Audio.get("musHandsomePrinceIntro").setLooping(true);
			Audio.get("musHandsomePrince").setGain(0);
			Audio.get("musHandsomePrinceIntro").setGain(0);
			Audio.fade("musHandsomePrince", 0, 0);
			Audio.fade("musHandsomePrinceIntro", 0, 0);
			Sound.play("musHandsomePrince");
			Sound.play("musHandsomePrinceIntro");
			play = true;
		}
		
		//TODO fix muting
		
		if (Global.room(14, 5)){
			if (done){
				if (Audio.soundExists("musHandsomePrinceIntro"))
					Audio.fade("musHandsomePrinceIntro", 1.0, .015);
				if (Audio.soundExists("musHandsomePrince"))
					Audio.fade("musHandsomePrince", 0, .01);
			}
		}
		else if (Global.room(14, 4)){
			if (done){
				if (Audio.soundExists("musHandsomePrince")){
					if (!Scene.instanceExists(ChestScene.class) && !Scene.instanceExists(CellPhone.class))
						Audio.fade("musHandsomePrince", 1.0, .015);
					else
						Audio.fade("musHandsomePrince", 0, .015);
				}
				if (Audio.soundExists("musHandsomePrinceIntro"))
					Audio.fade("musHandsomePrinceIntro", 0, .01);
			}
		}
		else if (alarm[0] == -1){
			alarm[0] = 90;
			if (Audio.soundExists("musHandsomePrince"))
				Audio.fade("musHandsomePrince", 0, .011112);
			if (Audio.soundExists("musHandsomePrinceIntro"))
				Audio.fade("musHandsomePrinceIntro", 0, .011112);
		}
		
		alarmStep();
	}

}
