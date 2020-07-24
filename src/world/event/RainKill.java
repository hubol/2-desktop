package world.event;

import java.util.ArrayList;

import world.control.EV;
import world.control.Global;
import world.enemy.Urchin;
import main.Entity;
import main.Scene;

public class RainKill extends Entity{

	public RainKill(double x, double y) {
		super(x, y);
		if (Global.event[EV.RAINKILL] == 1){
			ArrayList<Urchin> list = Scene.getEntityList(Urchin.class);
			for (int i=0; i<list.size(); i++)
				list.get(i).destroy();
			destroy();
		}
		setCollisionGroup(Global.DEACTIVATEME);
		visible = false;
	}
	
	public void step(){
		if (Scene.instanceNumber(Urchin.class) == 0){
			Global.event[EV.RAINKILL] = 1;
			destroy();
		}
	}

}
