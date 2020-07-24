package world.event;

import world.control.Global;
import main.Entity;

public class MonsterBack extends Entity{
	public Monster mom;

	public MonsterBack(Monster mom) {
		super(0, 0);
		setDepth(1);
		setCollisionGroup(Global.DEACTIVATEME);
		
		this.mom = mom;
	}
	
	public void step(){
		
	}
	
	public void render(){
		mom.backRender();
	}

}
