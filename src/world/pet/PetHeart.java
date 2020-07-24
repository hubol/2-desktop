package world.pet;

import main.Calc;

public class PetHeart extends PetEntity{
	public int img;
	public double hsp, vsp, grav;

	public PetHeart(double x, double y, PetShell p) {
		super(x, y, p);
		img = (int)Calc.random(3);
		hsp = Calc.rangedRandom(2);
		vsp = -Calc.random(.8);
		grav = -.05 - Calc.random(.15);
	}
	
	public void step(){
		x += hsp;
		y += vsp;
		
		vsp += grav;
		
		if (y < -12)
			destroy();
	}
	
	public void render(){
		shell.heartEffect.render(img, x, y, shell);
	}

}
