package world.event;

import java.util.ArrayList;

import world.control.EV;
import world.control.Global;
import world.gameplay.Balloon;
import main.Entity;
import main.Scene;

public class MoreFunWithBalloons extends Entity{

	public MoreFunWithBalloons(double x, double y) {
		super(x, y);
		if (Global.event[EV.MOREFUNWITHBALLOONS] == 1){
			ArrayList<Balloon> list = Scene.getEntityList(Balloon.class);
			for (int i=0; i<list.size(); i++)
				list.get(i).destroy();
			destroy();
		}
		setCollisionGroup(Global.DEACTIVATEME);
		visible = false;
	}
	
	public void step(){
		if (Scene.instanceNumber(Balloon.class) == 0){
			Global.event[EV.MOREFUNWITHBALLOONS] = 1;
			destroy();
		}
	}

}
