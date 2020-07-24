package world.boss.puke;

import world.control.Global;
import main.Entity;

public class PukeSpawn extends Entity{

	public PukeSpawn(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		alarmInitialize(1);
		
		alarmEvent(0);
		visible = false;
	}
	
	public void alarmEvent(int i){
		new PukeBalloon(x, y, this);
	}

}
