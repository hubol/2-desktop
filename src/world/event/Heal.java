package world.event;

import world.control.Global;
import world.particles.HealParticles;
import main.Entity;

public class Heal extends Entity{
	public int count = 0;
	
	public final double particles = 9;
	public final double frames = 5;
	public final double spd = (360.0 / particles) / frames;

	public Heal() {
		super(0, 0);
		setCollisionGroup(Global.DEACTIVATEME);
		
		visible = false;
		alarmInitialize(1);
		alarmEvent(0);
	}
	
	public void alarmEvent(int i){
		new HealParticles(spd);
		count += 1;
		if (count >= particles)
			destroy();
		else
			alarm[0] = (int)frames;
	}
	
	public void step(){
		alarmStep();
	}
	
	public void render(){
		
	}

}
