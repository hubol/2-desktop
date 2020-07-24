package world.junk;

import world.control.EV;
import world.control.Global;
import main.Entity;

public class KissFaceControl extends Entity{
	public int phase = 0;

	public KissFaceControl(double x, double y) {
		super(x, y);
		new KissFaceUnder(x - 128, y, false, (Global.event[EV.KISSADD] == 1), this);
		new KissFaceUnder(x + 128, y, true, (Global.event[EV.KISSADD] == 1), this);
	}
	
	/**called by faces when hit by gunfire*/
	public boolean awaken(boolean right){
		if (phase == 0 && right){
			phase = 1;
			return true;
		}
		
		if (phase == 1 && !right){
			Global.event[EV.KISSADD] = 1; //TODO insound for addblock
			phase = 2;
			return true;
		}
		
		return false;
	}

}
