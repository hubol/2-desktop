package world.boss.puke;

import world.control.EV;
import world.control.Global;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import main.Entity;

public class PukeInit extends Entity{

	public PukeInit(double x, double y) {
		super(x, y);
		
		if (Global.event[EV.PUKE_DEFEAT] == 0){
			new SpriteLoader("sPukeMask", "sPuke_6", "sPukeDead", "sPukeHurt", "sPukeShadow_6");
			new SoundLoader("sDrama", "sPukeFlap1", "sPukeFlap2", "sPukePeep1", "sPukePeep2", "sPukePeep3", "sPukePeep4", "sBossTextPuke", "musBoss05", "musBoss05FirstForm", "sPukeDie", "sSkullQuake", "sPukeDeathtangle", "sPukeNext", "sPukeScream", "sPukeDeathScream", "sPukePop", "sPukeEnd");
		}
		else
			destroy();
		
		visible = false;
	}
	
	public void step(){
		
	}

}
