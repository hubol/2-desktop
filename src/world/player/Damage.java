package world.player;

import world.control.Global;
import graphics.Graphics;
import graphics.Text;
import main.Calc;
import main.Entity;

public class Damage extends Entity{
	public String text;
	
	public int glitchAmount = 12;
	public double glitchTimer;
	public boolean[] glitchActive;
	
	public double intensity = 1;

	public Damage(double x, double y, int amt) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth( Integer.MIN_VALUE + 5 );
		
		//text = Calc.formatNumberString(""+amt,3);
		text = ""+amt;
		vspeed = -8;
		
		glitchActive = new boolean[glitchAmount];
		glitchReset();
	}
	
	public Damage(double x, double y, String s) {
		super(x, y);
		setDepth( Integer.MIN_VALUE + 19 );
		
		//text = Calc.formatNumberString(""+amt,3);
		text = s.toUpperCase();
		vspeed = -8;
		
		glitchActive = new boolean[glitchAmount];
		glitchReset();
	}
	
	public void step(){
		x += Calc.rangedRandom(.75);
		y += Calc.rangedRandom(.75);
		
		super.step();
		vspeed *= .8;
		if (vspeed < 1.2){
			intensity += .05;
			alpha -= .02;
			if (alpha <= 0)
				destroy();
		}
		
		glitchTimer -= 1;
		if (glitchTimer <= 0)
			glitchReset();
	}
	
	public void glitchReset(){
		for (int i=0; i<glitchAmount; i++)
			glitchActive[i] = false;
		
		int amount = (Integer)Calc.choose(0,0,1,1,1,1,1,2,2,2,3,3,4,5);
		for (int i=0; i<amount; i++)
			glitchActive[(int)Calc.random(glitchAmount)] = true;
		
		glitchTimer = Calc.random(60);
	}
	
	public void render(){
		double xx = x, yy = y, h = 1, r = .5, xsc = 1, ysc = 1, ang = 0, alp = alpha;
		//String col = "FF3C35", shCol = "006187";
		int[] col = Calc.getColorHex("FF3C35"), shCol = Calc.getColorHex("006187");
		int draws = 1;
		
		double mult = .5;
		
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
				xsc = .9 + Calc.random(.2 * intensity);
			if (glitchActive[5])
				xsc = .9 + Calc.random(.2 * intensity);
			if (glitchActive[6])
				ang += Calc.rangedRandom(4 * intensity);
			if (glitchActive[7])
				alp *= Calc.random(1) * intensity;
			if (glitchActive[8] || glitchActive[10]){
				for (int j=0; j<3; j++)
					col[j] = (int)Calc.random(256);
			}
			if (glitchActive[9] || glitchActive[10]){
				for (int j=0; j<3; j++)
					shCol[j] = (int)Calc.random(256);
			}
			
			Text.idiot = false;
			Text.setFont(Global.FONT);
			Text.orientation = Text.CENTERED;
			Text.setSpacing(h, 1);
			Text.randomize(r);
			Graphics.setAlpha(alp);
			Graphics.setColor(shCol);
			double dist = 2;
			Text.drawTextExt(xx - dist + Calc.rangedRandom(r), yy + dist + Calc.rangedRandom(r), text, xsc * mult, ysc * mult, ang);
			Text.drawTextExt(xx + dist + Calc.rangedRandom(r), yy + dist + Calc.rangedRandom(r), text, xsc * mult, ysc * mult, ang);
			Text.drawTextExt(xx - dist + Calc.rangedRandom(r), yy - dist + Calc.rangedRandom(r), text, xsc * mult, ysc * mult, ang);
			Text.drawTextExt(xx + dist + Calc.rangedRandom(r), yy - dist + Calc.rangedRandom(r), text, xsc * mult, ysc * mult, ang);
			Text.drawTextExt(xx - dist + Calc.rangedRandom(r), yy + Calc.rangedRandom(r), text, xsc * mult, ysc * mult, ang);
			Text.drawTextExt(xx + dist + Calc.rangedRandom(r), yy + Calc.rangedRandom(r), text, xsc * mult, ysc * mult, ang);
			Text.drawTextExt(xx + Calc.rangedRandom(r), yy + dist + Calc.rangedRandom(r), text, xsc * mult, ysc * mult, ang);
			Text.drawTextExt(xx + Calc.rangedRandom(r), yy - dist + Calc.rangedRandom(r), text, xsc * mult, ysc * mult, ang);
			Graphics.setColor(col);
			Text.drawTextExt(xx + Calc.rangedRandom(r), yy + Calc.rangedRandom(r), text, xsc * mult, ysc * mult, ang);
		}
		
		Text.idiot = true;
		Text.randomize(1);
		Text.setSpacing(1, 1);
		Graphics.setAlpha(1);
	}

}
