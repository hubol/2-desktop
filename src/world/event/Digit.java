package world.event;

import world.control.Global;
import world.control.Sound;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class Digit extends Entity{
	public boolean[][] image = new boolean[10][15];
	public int[] change = new int[15];
	
	public int id, digit;
	
	public boolean fall;
	public double[] hsp, vsp, xx, yy;

	public Digit(double x, double y, int id, int digit) {
		super(x, y);
		
		hsp = new double[15];
		vsp = new double[15];
		xx = new double[15];
		yy = new double[15];
		
		//hemseprep
		for (int i=0; i<10; i++){
			for (int j=0; j<15; j++){
				image[i][j] = false;
				hsp[j] = 0;
				vsp[j] = 0;
				xx[j] = 0;
				yy[j] = 0;
			}
		}
		
		fall = false;
		
		//omfg
		image[0][0] = true; image[0][3] = true; image[0][6] = true; image[0][9] = true; image[0][12] = true; image[0][13] = true; image[0][14] = true; image[0][11] = true; image[0][8] = true; image[0][5] = true; image[0][2] = true; image[0][1] = true; image[1][1] = true; image[1][4] = true; image[1][7] = true; image[1][10] = true; image[1][13] = true; image[2][0] = true; image[2][1] = true; image[2][2] = true; image[2][5] = true; image[2][8] = true; image[2][7] = true; image[2][6] = true; image[2][9] = true; image[2][12] = true; image[2][13] = true; image[2][14] = true; image[3][14] = true; image[3][11] = true; image[3][8] = true; image[3][5] = true; image[3][2] = true; image[3][1] = true; image[3][0] = true; image[3][7] = true; image[3][6] = true; image[3][13] = true; image[3][12] = true; image[4][0] = true; image[4][3] = true; image[4][6] = true; image[4][7] = true; image[4][8] = true; image[4][2] = true; image[4][5] = true; image[4][14] = true; image[4][11] = true; image[5][2] = true; image[5][1] = true; image[5][0] = true; image[5][3] = true; image[5][6] = true; image[5][7] = true; image[5][8] = true; image[5][11] = true; image[5][14] = true; image[5][13] = true; image[5][12] = true; image[6][0] = true; image[6][3] = true; image[6][6] = true; image[6][9] = true; image[6][12] = true; image[6][13] = true; image[6][14] = true; image[6][11] = true; image[6][8] = true; image[6][7] = true; image[6][1] = true; image[6][2] = true; image[7][0] = true; image[7][1] = true; image[7][2] = true; image[7][5] = true; image[7][8] = true; image[7][11] = true; image[7][14] = true; image[8][12] = true; image[8][9] = true; image[8][6] = true; image[8][3] = true; image[8][0] = true; image[8][1] = true; image[8][2] = true; image[8][5] = true; image[8][8] = true; image[8][11] = true; image[8][14] = true; image[8][13] = true; image[8][7] = true; image[9][8] = true; image[9][7] = true; image[9][6] = true; image[9][3] = true; image[9][0] = true; image[9][1] = true; image[9][2] = true; image[9][5] = true; image[9][11] = true; image[9][14] = true; image[9][13] = true; image[9][12] = true; 
		
		for (int i=0; i<15; i++)
				change[i] = -1;
		
		this.id = id;
		this.digit = digit;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE+19);
	}
	
	public void shatter(){
		if (!fall){
			Global.explosionEffect(x + 16, y + 32, 1.4);
			Sound.playPitched("sBombBlast",.05);
			Sound.playPitched("sDishSplode",.08);
			
			//TODO sound??
			fall = true;
			
			for (int i=0; i<15; i++){
				hsp[i] = Calc.rangedRandom(5);
				vsp[i] = -1 - Calc.random(8);
			}
		}
	}
	
	public void changeDigit(int toDig){
		digit = toDig;
		for (int i=0; i<15; i++){
				change[i] = 2 + (int)Calc.random(5);
			if (image[digit][i])
				change[i] = 5 + (int)Calc.random(7);
		}
	}
	
	public void step(){
		for (int i=0; i<15; i++){
			if (change[i] > -1)
				change[i] -= 1;
			
			if (fall){
				xx[i] += hsp[i];
				yy[i] += vsp[i];
				vsp[i] += .8;
				hsp[i] *= .96;
			}
		}
	}
	
	public void render(){
		final double width = 32.0, height = 64.0;
		final double xsc = width / 48.0, ysc = height / 80.0;
		double xx = 0, yy = 0;
		
		for (int i=0; i<3; i++){
			yy = 0;
			for (int j=0; j<5; j++){
				boolean draw = image[digit][i + (j * 3)];
				if (change[i + (j * 3)] > -1)
					draw = (Boolean)Calc.choose(true, false);
				
				if (draw)
					Sprite.get("sDigitDot").render(0, Sprite.NORTHWEST, x + xx + Calc.rangedRandom(.3) + this.xx[i + (j * 3)], y + yy + Calc.rangedRandom(.3) + this.yy[i + (j * 3)], xsc, ysc, Calc.rangedRandom(1), 1, 1, 1, 1);
				yy += height / 5.0;
			}
			xx += width / 3.0;
		}
	}

}
