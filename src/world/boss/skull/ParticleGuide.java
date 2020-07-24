package world.boss.skull;

import world.control.Global;
import main.Calc;
import main.Entity;

public class ParticleGuide extends Entity{
	public double toX, toY;
	public double div;
	public boolean special;

	public ParticleGuide(double x, double y, double a, double b, double div, boolean s) {
		super(x, y);
		toX = a; toY = b;
		setCollisionGroup(Global.DEACTIVATEME);
		
		special = s;
		
		this.div = div;
		visible = false;
	}
	
	public void step(){
		for (int i=0; i<2; i++)
			new PetalParticle(x, y);
		
		x = Calc.approach(x, toX, div);
		y = Calc.approach(y, toY, div);
		
		div -= .1;
		div = Math.max(1, Calc.approach(div, 1, 15));
		
		if (Calc.pointDistance(x, y, toX, toY) < 4){
			new SkullHole(toX, toY, special);
			
			destroy();
		}
	}
	
	public void render(){
		
	}

}
