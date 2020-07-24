package world.event;

import java.util.ArrayList;

import audio.Audio;
import world.Fg;
import world.control.Global;
import world.control.Music;
import world.player.Player;
import main.Entity;
import main.Scene;

public class DishPuzzleComplete extends Entity{
	public double pitch;
	public int count;

	public DishPuzzleComplete() {
		super(0, 0);
		Fg.me.shakeTimer = 69000; //reset it when done
		
		Audio.fade("musChimey", 0, .007);
		
		pitch = 1;
		
		alarmInitialize(1);
		alarm[0] = 22;
	}
	
	public void alarmEvent(int j){
		if (j == 0){
			if (count < 3){
				ArrayList<Digit> list = Scene.getEntityList(Digit.class);
				for (int i=0; i<list.size(); i++){
					if (list.get(i).id == count)
						list.get(i).shatter();
				}
			}
			else if (count < 7){
				ArrayList<DishBlock> list = Scene.getEntityList(DishBlock.class);
				for (int i=0; i<list.size(); i++){
					if (list.get(i).id == count - 3)
						list.get(i).brekken();
				}
			}
			else{
				//TODO appear sound??
				new DassKey(288, 64, true);
				
				Global.roomMusic = "musMirrorDance";
				Music.fadeMusic(Global.roomMusic, true);
				Player.control = true;
				Fg.me.shakeTimer = 5;
				destroy();
			}
			count += 1;
			
			if (count < 8){
				alarm[0] = 22 - (count * 2);
			}
		}
	}
	
	public void step(){
		pitch += .001;
		pitch *= 1.001;
		if (Audio.soundExists("musChimey"))
			Audio.get("musChimey").setPitch(pitch);
		
		alarmStep();
	}

}
