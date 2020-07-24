package world.control;

import main.Entity;

public class VLoop extends Entity{

	public VLoop(double x, double y) {
		super(x, y);
		visible = false;
		setCollisionGroup(Global.DEACTIVATEME, Global.CONTROLLER);
	}

}
