package world.water;

import main.Calc;
import main.Entity;

public class Splash extends Entity{

	public Splash(double x, double y, int amt, double size, double spd) {
		super(x, y);
		for (int i=0; i<amt; i++)
			new SplashParts(x, y, Calc.random(360), spd * ( 1 + Calc.rangedRandom(.1) ), size * ( 1 + Calc.rangedRandom(.1) ));
		destroy();
	}
	
	public Splash(double x, double y, int amt, double size, double spd, double fjuk) {
		super(x, y);
		for (int i=0; i<amt; i++)
			new SplashParts(x, y, fjuk + Calc.rangedRandom(60), spd * ( 1 + Calc.rangedRandom(.1) ), size * ( 1 + Calc.rangedRandom(.1) ));
		destroy();
	}

}
