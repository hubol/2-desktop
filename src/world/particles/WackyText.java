package world.particles;

import world.control.Global;
import graphics.Graphics;
import graphics.Text;
import main.Calc;
import main.Entity;

public class WackyText extends Entity{
	public String text;
	
	public int glitchAmount = 12;
	public double glitchTimer;
	public boolean[] glitchActive;
	
	public double intensity = 1;
	
	public int letters;
	public double[] xx, yy, dir, spd, ang, add;

	public WackyText(double x, double y, String s) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth( Integer.MIN_VALUE + 19 );
		
		//text = Calc.formatNumberString(""+amt,3);
		text = s.toUpperCase();
		vspeed = -8;
		
		letters = text.length();
		xx = new double[letters];
		yy = new double[letters];
		dir = new double[letters];
		spd = new double[letters];
		ang = new double[letters];
		add = new double[letters];
		
		for (int i=0; i<letters; i++){
			xx[i] = 0;
			yy[i] = 0;
			dir[i] = 160.0 - (((double)i / (double)(letters - 1.0))*160.0) + Calc.rangedRandom(8);
			spd[i] = .2 + Calc.rangedRandom(.05);
			ang[i] = 0;
			add[i] = .5 - (((double)i / (double)(letters - 1.0))*.5);
		}
		
		glitchActive = new boolean[glitchAmount];
		glitchReset();
	}
	
	public void step(){
		x += Calc.rangedRandom(.75);
		y += Calc.rangedRandom(.75);
		
		super.step();
		vspeed *= .8;
		if (vspeed < .6){
			for (int i=0; i<letters; i++){
				xx[i] += Calc.dirX(spd[i], dir[i]);
				yy[i] += Calc.dirY(spd[i], dir[i]);
				ang[i] += add[i];
			}
			
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
		int[] col = Calc.getColorHex(Global.roomColor), shCol = Calc.getColorHex("006187");
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
			
			Text.setFont(Global.FONT);
			Text.orientation = Text.CENTERED;
			Text.setSpacing(h, 1);
			double dist = 2;
			for (int k=0; k<letters; k++){
				String t = "";
				for (int l=0; l<k; l++)
					t += " ";
				t += text.charAt(k);
				for (int l=k + 1; l<letters; l++)
					t += " ";
				
				Text.randomize(r);
				Graphics.setAlpha(alp);
				Graphics.setColor(shCol);
				Text.drawTextExt(this.xx[k] + xx - dist + Calc.rangedRandom(r), this.yy[k] + yy + dist + Calc.rangedRandom(r), t, xsc * mult, ysc * mult, this.ang[k] + ang);
				Text.drawTextExt(this.xx[k] + xx + dist + Calc.rangedRandom(r), this.yy[k] + yy + dist + Calc.rangedRandom(r), t, xsc * mult, ysc * mult, this.ang[k] + ang);
				Text.drawTextExt(this.xx[k] + xx - dist + Calc.rangedRandom(r), this.yy[k] + yy - dist + Calc.rangedRandom(r), t, xsc * mult, ysc * mult, this.ang[k] + ang);
				Text.drawTextExt(this.xx[k] + xx + dist + Calc.rangedRandom(r), this.yy[k] + yy - dist + Calc.rangedRandom(r), t, xsc * mult, ysc * mult, this.ang[k] + ang);
				Text.drawTextExt(this.xx[k] + xx - dist + Calc.rangedRandom(r), this.yy[k] + yy + Calc.rangedRandom(r), t, xsc * mult, ysc * mult, this.ang[k] + ang);
				Text.drawTextExt(this.xx[k] + xx + dist + Calc.rangedRandom(r), this.yy[k] + yy + Calc.rangedRandom(r), t, xsc * mult, ysc * mult, this.ang[k] + ang);
				Text.drawTextExt(this.xx[k] + xx + Calc.rangedRandom(r), this.yy[k] + yy + dist + Calc.rangedRandom(r), t, xsc * mult, ysc * mult, this.ang[k] + ang);
				Text.drawTextExt(this.xx[k] + xx + Calc.rangedRandom(r), this.yy[k] + yy - dist + Calc.rangedRandom(r), t, xsc * mult, ysc * mult, this.ang[k] + ang);
				Graphics.setColor(col);
				Text.drawTextExt(this.xx[k] + xx + Calc.rangedRandom(r), this.yy[k] + yy + Calc.rangedRandom(r), t, xsc * mult, ysc * mult, this.ang[k] + ang);
			}
		}
		
		Text.randomize(1);
		Text.setSpacing(1, 1);
		Graphics.setAlpha(1);
	}

}
