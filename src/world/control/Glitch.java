package world.control;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class Glitch extends Entity{
	public int glitchAmount = 12;
	public double glitchTimer;
	public boolean[] glitchActive;
	
	public double intensity = 1;

	public Glitch() {
		super(0, 0);
		glitchActive = new boolean[glitchAmount];
		glitchReset();
	}
	
	public void step(){
		x += Calc.rangedRandom(.25);
		y += Calc.rangedRandom(.25);

		glitchTimer -= 1;
		if (glitchTimer <= 0)
			glitchReset();
	}
	
	public void glitchReset(){
		x = 0;
		y = 0;
		
		for (int i=0; i<glitchAmount; i++)
			glitchActive[i] = false;
		
		int amount = (Integer)Calc.choose(0,0,1,1,1,1,1,2,2,2,3,3,4,5);
		for (int i=0; i<amount; i++)
			glitchActive[(int)Calc.random(glitchAmount)] = true;
		
		glitchTimer = Calc.random(60);
	}
	
	public void render(Entity i){
		render(i.sprite, i.imageSingle, i.orientation, i.x, i.y, i.xscale, i.yscale, i.angle, i.alpha, Calc.makeHexColor(i.colR,i.colG,i.colB));
	}
	
	public void render(Sprite s, double img, int orient, double x, double y, double xs, double ys, double aa, double alpu, String colour){
		double xx = x, yy = y, h = 1, r = .5, xsc = xs, ysc = ys, ang = aa, alp = alpu;
		int[] col = Calc.getColorHex(colour);
		int draws = 1;
		
		if (glitchActive[8])
			draws = (int)Calc.random(5) + 1;
		
		for (int i=0; i<draws; i++){
			if (glitchActive[0])
				xx += Calc.rangedRandom(4*intensity);
			if (glitchActive[1])
				yy += Calc.rangedRandom(4*intensity);
			if (glitchActive[2])
				h += Calc.random(.2*intensity);
			if (glitchActive[3])
				r += Calc.random(1 * intensity);
			if (glitchActive[4])
				xsc *= .9 + Calc.random(.2 * intensity);
			if (glitchActive[5])
				ysc *= .9 + Calc.random(.2 * intensity);
			if (glitchActive[6])
				ang += Calc.rangedRandom(4 * intensity);
			if (glitchActive[7])
				alp *= Calc.random(1) * intensity;
			if (glitchActive[8] || glitchActive[10]){
				for (int j=0; j<3; j++)
					col[j] = (int)Math.max(0, Math.min(((double)col[j] * (1 + Calc.rangedRandom(.25 * intensity))), 255));
			}
			if (glitchActive[9] || glitchActive[10]){
				yy += Calc.rangedRandom(Calc.random(8)*intensity);
			}
			
			s.render(img, orient, xx + Calc.random(r), yy + Calc.random(h), xsc, ysc, ang, alp, col[0] / 256.0, col[1] / 256.0, col[2] / 256.0);
		}

	}

}
