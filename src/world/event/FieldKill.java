package world.event;

import java.util.ArrayList;

import world.control.EV;
import world.control.Global;
import world.enemy.Urchin;
import main.Entity;
import main.Scene;

public class FieldKill extends Entity{
	public int at;
	public final String[] txt = {"kill","me","please"};

	public FieldKill(double x, double y) {
		super(x, y);
		at = 0;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		if (Global.event[EV.FIELD_KILL] == 1){
			ArrayList<Urchin> list = Scene.getEntityList(Urchin.class);
			for (int i=0; i<list.size(); i++)
				list.get(i).destroy();
			
			destroy();
		}
		else{
			alarmInitialize(1);
			alarm[0] = 1;
		}
	}
	
	public void alarmEvent(int i){
		ArrayList<Urchin> list = Scene.getEntityList(Urchin.class);
		
		if (list.size() > 2){
			new SmallMessage(list.get(at), 0, -18, txt[at], 9);
			
			alarm[0] = 12;
			if (at == 2)
				at = 0;
			else
				at += 1;
		}
	}
	
	public void step(){
		ArrayList<Urchin> list = Scene.getEntityList(Urchin.class);
		if (list.size() == 0){
			Global.event[EV.FIELD_KILL] = 1;
			destroy();
		}
		else
			super.step();
	}
	
	

}
