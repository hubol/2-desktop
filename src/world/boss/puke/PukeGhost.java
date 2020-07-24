package world.boss.puke;

import world.control.Global;
import world.control.Music;
import main.Entity;

public class PukeGhost extends Entity{

	public PukeGhost() {
		super(0, 0);
		alarmInitialize(1);
		alarm[0] = 30;
		visible = false;
	}
	
	public void alarmEvent(int i){
		Music.fadeMusic(Global.roomMusic, true);
		Global.blockFade(false);
		Global.heal();
		destroy();
	}

}
