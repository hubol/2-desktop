package world.boss.duke;

import world.control.EV;
import world.control.Global;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import main.Entity;

public class DukeInit extends Entity{

	public DukeInit(double x, double y) {
		super(x, y);
		
		if (Global.event[EV.DUKE_DEFEAT] == 0){
			new SpriteLoader("sPukeMask", "sDuke_6", "sDukeDead", "sDukeHurt", "sDukeShadow_6");
			new SoundLoader("sDukeDrama", "sPukeFlap1", "sPukeFlap2", "sDukePeep1", "sDukePeep2", "sDukePeep3", "sDukePeep4", "sBossTextDuke", "sBossTextPuke", "musBoss06", "musBoss06FirstForm", "sSkullQuake", "sDuketangle", "sDukeNext", "sDukeScream", "sPukeEnd", "sDukeDie", "sDukePop", "sDukeDeathScream");
		}
		else
			destroy();
		
		visible = false;
	}
	
	public void step(){
		
	}

}
