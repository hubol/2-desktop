package world.event;

import world.control.Global;
import main.Entity;

public class BathSparksSpawn extends Entity{
	public int id;

	public BathSparksSpawn(double x, double y, int id) {
		super(x, y);
		this.id = id;
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		
	}
	
	public void render(){
		
	}

}
