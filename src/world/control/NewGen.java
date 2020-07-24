package world.control;

import main.Entity;

public class NewGen extends Entity{
	public double a, b, c, add, seed, mod;
	public double mult;

	public NewGen(double a, double b, double c, double add, double mod, double seed) {
		super(0, 0);
		this.a = a;
		this.b = b;
		this.c = c;
		this.add = add;
		this.mod = mod;
		this.seed = seed;
		
		mult = 1;
		
		visible = false;
	}
	
	public void step(){	
	}
	
	public double random(double x){
		seed = Math.abs(((seed + (x * a)) * b) + (c * Math.sin(seed)) + (add * mult * x));
		mult *= -1;
		if (seed > Double.MAX_VALUE)
			seed = x;
		return x * ((double)(seed % mod)/mod);
	}
	
	public double rangedRandom(double x){
		return x - random(2 * x);
	}
	
	public Object choose(Object... entries){
		int i = (int)(random(1)*entries.length);
		return entries[i];
	}

}
