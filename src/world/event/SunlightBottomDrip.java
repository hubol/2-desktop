package world.event;

import world.control.Global;
import main.Calc;
import main.Entity;

public class SunlightBottomDrip extends Entity{
	public double timer;

	public SunlightBottomDrip(double x, double y) {
		super(x, y);
		timer = Calc.random(100);
		visible = false;
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		timer -= 1;
		if (timer <= 0){
			new SunlightBottomDrop(x + 16, y - 1);
			timer = 60 + Calc.random(140);
		}
	}

}
