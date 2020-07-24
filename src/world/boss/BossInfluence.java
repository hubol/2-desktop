package world.boss;

import world.control.Global;
import world.control.MapIconInfluence;

public class BossInfluence extends MapIconInfluence{
	public int eventId;

	public BossInfluence(double x, double y, int id) {
		super(x, y);
		visible = false;
		
		eventId = id;
	}
	
	public void refreshIcon(){
		if (!isDestroyed()){
			if (Global.event[eventId] > 0)
				Global.setIconMap(Global.roomX, Global.roomY, 6);
			else
				Global.setIconMap(Global.roomX, Global.roomY, 7);
		}
	}
	
	public void step(){
		
	}
	
	public void render(){
		
	}

}
