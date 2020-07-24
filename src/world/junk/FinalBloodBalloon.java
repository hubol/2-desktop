package world.junk;

import world.control.EV;
import world.control.Global;
import world.gameplay.Balloon;
import main.Entity;
import main.Scene;

public class FinalBloodBalloon extends Entity{

	public FinalBloodBalloon(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		visible = false;
	}
	
	public void step(){
		if (!Scene.instanceExists(Balloon.class))
			Global.event[EV.FINALBLOODBALLOON] = 1;
	}

}
