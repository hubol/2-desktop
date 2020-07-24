package world.boss.banaan;

import world.control.Global;
import main.Entity;

public class BanaanVPuker extends Entity{
	public double distance, vsp;

	public BanaanVPuker(double x, double y, double v, double dist) {
		super(x, y);
		distance = dist;
		vsp = v;
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		new BanaanVPuke(x, y, vsp);
		distance -= Math.abs(vsp);
		if (distance <= 0)
			destroy();
	}
	
	public void render(){
		
	}

}
