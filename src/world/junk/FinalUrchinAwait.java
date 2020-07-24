package world.junk;

import world.control.EV;
import world.control.Global;
import world.enemy.Urchin;
import main.Entity;
import main.Scene;

public class FinalUrchinAwait extends Entity{

	public FinalUrchinAwait(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		visible = false;
	}
	
	public void step(){
		if (!Scene.instanceExists(Urchin.class))
			Global.event[EV.FINALURCHIN] = 1;
	}

}
