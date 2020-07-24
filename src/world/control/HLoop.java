package world.control;

import main.Entity;

public class HLoop extends Entity{

	public HLoop(double x, double y) {
		super(x, y);
		visible = false;
		setCollisionGroup(Global.DEACTIVATEME, Global.CONTROLLER);
	}

}
