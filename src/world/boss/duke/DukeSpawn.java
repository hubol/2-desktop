package world.boss.duke;

import world.control.Global;
import main.Entity;

public class DukeSpawn extends Entity{

	public DukeSpawn(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		alarmInitialize(1);
		
		alarmEvent(0);
		visible = false;
	}
	
	public void alarmEvent(int i){
		new DukeBalloon(x, y, this);
	}

}
