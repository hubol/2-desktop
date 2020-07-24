package world.junk;

import world.control.EV;
import world.control.Global;
import world.gameplay.Balloon;
import main.Entity;
import main.Scene;

public class FinalEnter extends Entity{

	public FinalEnter(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		visible = false;
	}
	
	public void step(){
		if (!Scene.instanceExists(Balloon.class))
			Global.event[EV.FINALENTER] = 1;
	}

}
