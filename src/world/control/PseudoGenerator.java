package world.control;

import main.Entity;

public class PseudoGenerator extends Entity{
	public double numberGen;
	public double multA, multB, add, keepPositiveAdd, outOfBounds, modulo;
	public boolean a = false;

	public PseudoGenerator(double multipliera, double multiplierb, double adder, double keepPos, double out, double mod, double seed) {
		super(0, 0);
		prep(multipliera, multiplierb, adder, keepPos, out, mod, seed);
	}
	
	public PseudoGenerator(double multipliera, double multiplierb, double adder, double keepPos, double out, double mod, double seed, boolean ff) {
		super(0, 0);
		a = ff;
		prep(multipliera, multiplierb, adder, keepPos, out, mod, seed);
	}
	
	public void prep(double multipliera, double multiplierb, double adder, double keepPos, double out, double mod, double seed){
		multA = multipliera;
		multB = multiplierb;
		add = adder;
		keepPositiveAdd = keepPos;
		outOfBounds = out;
		modulo = mod;
		
		numberGen = seed;
		
		visible = false;
	}
	
	public double random(double a){
		numberGen += a * multA;
		numberGen += add;
		numberGen *= multB;
		
		if (numberGen < 0)
			numberGen += keepPositiveAdd;
		
		if (Math.abs(numberGen) > Double.MAX_VALUE - 1000)
			numberGen = outOfBounds;
		
		if (this.a)
			numberGen = Math.abs(numberGen);
		
		return ((Math.abs(numberGen) % modulo) / modulo) * a;
	}
	
	public double random(double a, double b){
		double min = b, max = a;
		if (a < b){
			min = a;
			max = b;
		}
		return min + random(max - min);
	}
	
	public double rangedRandom(double a){
		return -a + random(a * 2);
	}
	
	public Object choose(Object... entries){
		int i = (int)(random(1)*entries.length);
		return entries[i];
	}

}
