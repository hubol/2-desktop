package world.boss.papa;

import main.Calc;

public class Break{
	public double addDirection, ysc = (Double)Calc.choose(1.0, -1.0) * ( .9 + Calc.rangedRandom(.24)), xsc = .7 + Calc.rangedRandom(.5);
	public int img = (int)Calc.random(6);
	
	public Break(double dir){
		addDirection = dir;
	}

}
