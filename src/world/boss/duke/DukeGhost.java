package world.boss.duke;

import world.control.Global;
import world.control.Music;
import world.player.Player;
import main.Entity;

public class DukeGhost extends Entity{

	public DukeGhost() {
		super(0, 0);
		alarmInitialize(1);
		alarm[0] = 30;
		visible = false;
	}
	
	public void alarmEvent(int i){
		if (Player.me.x > 336 || Player.me.x < 304){
			Music.fadeMusic(Global.roomMusic, true);
			Global.blockFade(false);
			Global.heal();
			destroy();
		}
		else
			alarm[0] = 1;
	}

}
