package world.junk;

import world.control.Global;
import world.control.SpriteLoader;
import main.Entity;

public class SprakSpawn extends Entity{
	private boolean fuckYou = false;

	public SprakSpawn(double x, double y) {
		super(x, y);
		new SpriteLoader("sSprak");
		
		setCollisionGroup(Global.DEACTIVATEME);
		visible = false;
		alarmInitialize(1);
		alarmEvent(0);
	}
	 
	public void alarmEvent(int i){
		if (fuckYou)
			new Sprak(x, y + 32);
		else{
			new Sprak(x, y - 96);
			fuckYou = true;
		}
		alarm[0] = 174;
	}
	
	public void step(){
		alarmStep();
	}

}
