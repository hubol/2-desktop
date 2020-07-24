package world.control;

import main.Calc;
import main.Entity;

public class Shake extends Entity{
	public double spd;

	public Shake() {
		this(.8);
	}
	
	public Shake(double s) {
		super(0, 0);
		spd = s;
		alarmInitialize(2);
		alarm[0] = 1 + (int)Calc.random(30);
		alarm[1] = 1 + (int)Calc.random(30);
	}
	
	public void alarmEvent(int i){
		if (i == 0)
			x = Calc.approach(x, 0, 1.6);
		else
			y = Calc.approach(y, 0, 1.6);
		alarm[i] = 1 + (int)Calc.random(30);
	}
	
	public void step(){
		x += Calc.rangedRandom(spd);
		y += Calc.rangedRandom(spd);
		alarmStep();
	}
	
	public void render(){
		//void
	}

}
