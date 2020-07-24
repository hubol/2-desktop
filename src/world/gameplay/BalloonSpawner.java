package world.gameplay;

import world.control.Global;
import main.Calc;
import main.Entity;

public class BalloonSpawner extends Entity{
	public int eventWatch=-1, valueWatch=-1;
	public boolean create;

	public BalloonSpawner(double x, double y, int e, int v) {
		super(x, y);
		visible = false;
		eventWatch = e;
		valueWatch = v;
		create = true;
		
		check();
	}
	
	public void step(){
		create = false;
		check();
	}
	
	public void check(){
		if (eventWatch > -1){
			if (Global.event[eventWatch] == valueWatch){
				new Balloon(x,y);
				if (!create){ //if you werent just created let's make some particles
					//TODO sound?
					for (int i=0; i<4; i++)
						Global.squareParticle(x + Calc.rangedRandom(6), y + Calc.rangedRandom(6), 3 + (int)Calc.random(5), Global.roomColor, 1.5 + Calc.random(5));
				}
				destroy();
			}
		}
	}

}
