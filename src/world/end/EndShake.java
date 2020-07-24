package world.end;

import main.Calc;
import main.Entity;

public class EndShake extends Entity{
	public double shake = 0;

	public EndShake() {
		super(0, 0);
		visible = false;
	}
	
	public void step(){
		x = Calc.rangedRandom(shake);
		y = Calc.rangedRandom(shake);
		
		shake *= .9;
	}

}
