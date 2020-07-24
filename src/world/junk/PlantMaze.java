package world.junk;

import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.IO;
import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import main.Calc;
import main.Entity;
import main.Scene;

public class PlantMaze extends Entity{
	public MiniMaze myMaze;
	public final double dX = 448, dY = 32, w = 128, h = 128;
	public final double pW = w / 20.0, pH = h / 20.0;
	
	public double a = 0;
	public double add = 0;
	
	public int[][] color = new int[][]{Calc.getColorHex("C2FF78"), Calc.getColorHex("58C8FF"), Calc.getColorHex("D578FF"), Calc.getColorHex("D7FB2C")};
	//nothing, player, block, win
	
	public boolean lastTouch = false;
	
	public Shake s = new Shake(.7);

	public PlantMaze(double x, double y) {
		super(x, y + 3);
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE + 10);
		
		mask = Global.sBLOCK.mask;
		orientation = Sprite.NORTHWEST;
		
		new SoundLoader("sMiniMazeMove", "sMiniMazeFail", "sMiniMazeWin", "sMiniMazePower");
		
		if (Global.event[EV.PLANTMAZE] == 0){
			int[][] dingus = new int[20][20];
			for (int i=0; i<20; i++){
				for (int j=0; j<20; j++)
					dingus[i][j] = 0;
			}
			
			dingus[1][1] = 2; dingus[2][1] = 2; dingus[3][1] = 2; dingus[4][1] = 2; dingus[5][1] = 2; dingus[6][1] = 2; dingus[7][1] = 2; dingus[8][1] = 2; dingus[9][1] = 2; dingus[10][1] = 2; dingus[11][1] = 2; dingus[13][1] = 2; dingus[1][2] = 2; dingus[11][2] = 2; dingus[13][2] = 2; dingus[1][3] = 2; dingus[2][3] = 2; dingus[3][3] = 2; dingus[4][3] = 2; dingus[5][3] = 2; dingus[6][3] = 2; dingus[7][3] = 2; dingus[8][3] = 2; dingus[9][3] = 2; dingus[11][3] = 2; dingus[13][3] = 2; dingus[1][4] = 2; dingus[7][4] = 2; dingus[11][4] = 2; dingus[13][4] = 2; dingus[13][5] = 2; dingus[11][5] = 2; dingus[10][5] = 2; dingus[9][5] = 2; dingus[7][5] = 2; dingus[5][5] = 2; dingus[4][5] = 2; dingus[3][5] = 2; dingus[1][5] = 2; dingus[1][6] = 2; dingus[3][6] = 2; dingus[5][6] = 2; dingus[7][6] = 2; dingus[11][6] = 2; dingus[13][6] = 2; dingus[1][7] = 2; dingus[3][7] = 2; dingus[5][7] = 2; dingus[7][7] = 2; dingus[8][7] = 2; dingus[9][7] = 2; dingus[11][7] = 2; dingus[13][7] = 2; dingus[1][8] = 2; dingus[3][8] = 2; dingus[5][8] = 2; dingus[11][8] = 2; dingus[13][8] = 2; dingus[14][8] = 3; dingus[1][9] = 2; dingus[3][9] = 2; dingus[5][9] = 2; dingus[6][9] = 2; dingus[7][9] = 2; dingus[8][9] = 2; dingus[9][9] = 2; dingus[10][9] = 2; dingus[11][9] = 2; dingus[13][9] = 2; dingus[14][9] = 2; dingus[1][10] = 2; dingus[3][10] = 2; dingus[14][1] = 2; dingus[15][1] = 2; dingus[15][3] = 2; dingus[15][4] = 2; dingus[15][5] = 2; dingus[15][6] = 2; dingus[15][7] = 2; dingus[15][8] = 2; dingus[15][9] = 2; dingus[16][10] = 2; dingus[18][10] = 2; dingus[16][1] = 2; dingus[16][3] = 2; dingus[16][4] = 2; dingus[16][5] = 2; dingus[16][6] = 2; dingus[16][7] = 2; dingus[16][8] = 2; dingus[16][9] = 2; dingus[18][9] = 2; dingus[18][8] = 2; dingus[18][7] = 2; dingus[18][6] = 2; dingus[18][5] = 2; dingus[18][4] = 2; dingus[18][3] = 2; dingus[18][2] = 2; dingus[18][1] = 2; dingus[17][1] = 2; dingus[1][11] = 2; dingus[3][11] = 2; dingus[4][11] = 2; dingus[5][11] = 2; dingus[6][11] = 2; dingus[7][11] = 2; dingus[8][11] = 2; dingus[9][11] = 2; dingus[10][11] = 2; dingus[11][11] = 2; dingus[12][11] = 2; dingus[13][11] = 2; dingus[14][11] = 2; dingus[15][11] = 2; dingus[16][11] = 2; dingus[18][11] = 2; dingus[18][12] = 2; dingus[16][12] = 2; dingus[15][12] = 2; dingus[13][13] = 2; dingus[11][13] = 2; dingus[9][13] = 2; dingus[7][13] = 2; dingus[5][13] = 2; dingus[3][13] = 2; dingus[1][13] = 2; dingus[1][12] = 2; dingus[3][12] = 2; dingus[7][12] = 2; dingus[11][12] = 2; dingus[15][13] = 2; dingus[16][13] = 2; dingus[18][13] = 2; dingus[18][14] = 2; dingus[16][14] = 2; dingus[15][14] = 2; dingus[13][14] = 2; dingus[11][14] = 2; dingus[9][14] = 2; dingus[7][14] = 2; dingus[5][14] = 2; dingus[3][14] = 2; dingus[1][14] = 2; dingus[1][15] = 2; dingus[3][15] = 2; dingus[5][15] = 2; dingus[7][15] = 2; dingus[9][15] = 2; dingus[11][15] = 2; dingus[13][15] = 2; dingus[15][15] = 2; dingus[16][15] = 2; dingus[18][15] = 2; dingus[18][16] = 2; dingus[16][16] = 2; dingus[15][16] = 2; dingus[13][16] = 2; dingus[11][16] = 2; dingus[9][16] = 2; dingus[7][16] = 2; dingus[5][16] = 2; dingus[3][16] = 2; dingus[1][16] = 2; dingus[1][17] = 2; dingus[5][17] = 2; dingus[9][17] = 2; dingus[13][17] = 2; dingus[18][17] = 2; dingus[18][18] = 2; dingus[17][18] = 2; dingus[16][18] = 2; dingus[15][18] = 2; dingus[14][18] = 2; dingus[13][18] = 2; dingus[12][18] = 2; dingus[11][18] = 2; dingus[10][18] = 2; dingus[9][18] = 2; dingus[8][18] = 2; dingus[7][18] = 2; dingus[6][18] = 2; dingus[5][18] = 2; dingus[4][18] = 2; dingus[3][18] = 2; dingus[2][18] = 2; dingus[1][18] = 2;
			myMaze = new MiniMaze(2, 2, dingus);
		}
		else
			myMaze = heart();
		
		a = Global.storedA;
	}
	
	public void destroy(){
		Global.storedA = a;
		super.destroy();
	}
	
	public void roomDestroy(){
		Global.storedA = a;
		super.roomDestroy();
	}
	
	public MiniMaze heart(){
		int[][] dingus = new int[20][20];
		for (int i=0; i<20; i++){
		for (int j=0; j<20; j++)
		dingus[i][j] = 0;
		}dingus[1][0] = 2; dingus[2][0] = 2; dingus[3][0] = 2; dingus[4][0] = 2; dingus[5][0] = 2; dingus[6][0] = 2; dingus[7][0] = 2; dingus[8][0] = 2; dingus[9][0] = 2; dingus[10][0] = 2; dingus[11][0] = 2; dingus[12][0] = 2; dingus[13][0] = 2; dingus[0][1] = 2; dingus[1][1] = 2; dingus[2][1] = 2; dingus[3][1] = 2; dingus[4][1] = 2; dingus[5][1] = 2; dingus[6][1] = 2; dingus[7][1] = 2; dingus[8][1] = 2; dingus[9][1] = 2; dingus[10][1] = 2; dingus[11][1] = 2; dingus[12][1] = 2; dingus[13][1] = 2; dingus[0][2] = 2; dingus[1][2] = 2; dingus[2][2] = 2; dingus[3][2] = 2; dingus[8][2] = 2; dingus[9][2] = 2; dingus[10][2] = 2; dingus[11][2] = 2; dingus[0][3] = 2; dingus[1][3] = 2; dingus[2][3] = 2; dingus[8][3] = 2; dingus[9][3] = 2; dingus[10][3] = 2; dingus[11][3] = 2; dingus[0][4] = 2; dingus[1][4] = 2; dingus[2][4] = 2; dingus[9][4] = 2; dingus[10][4] = 2; dingus[10][5] = 2; dingus[9][5] = 2; dingus[1][5] = 2; dingus[0][5] = 2; dingus[0][6] = 2; dingus[1][6] = 2; dingus[0][7] = 2; dingus[1][7] = 2; dingus[0][8] = 2; dingus[1][8] = 2; dingus[0][9] = 2; dingus[1][9] = 2; dingus[0][10] = 2; dingus[1][10] = 2; dingus[2][10] = 2; dingus[14][1] = 2; dingus[14][0] = 2; dingus[15][0] = 2; dingus[15][1] = 2; dingus[17][10] = 2; dingus[18][10] = 2; dingus[19][10] = 2; dingus[19][9] = 2; dingus[19][8] = 2; dingus[19][7] = 2; dingus[19][6] = 2; dingus[19][5] = 2; dingus[19][4] = 2; dingus[19][3] = 2; dingus[19][2] = 2; dingus[19][1] = 2; dingus[19][0] = 2; dingus[18][0] = 2; dingus[17][0] = 2; dingus[16][0] = 2; dingus[16][1] = 2; dingus[16][2] = 2; dingus[18][9] = 2; dingus[18][8] = 2; dingus[18][7] = 2; dingus[18][6] = 2; dingus[18][5] = 2; dingus[18][4] = 2; dingus[18][3] = 2; dingus[18][2] = 2; dingus[18][1] = 2; dingus[17][1] = 2; dingus[17][2] = 2; dingus[17][3] = 2; dingus[17][4] = 2; dingus[0][11] = 2; dingus[1][11] = 2; dingus[2][11] = 2; dingus[17][11] = 2; dingus[18][11] = 2; dingus[19][11] = 2; dingus[19][12] = 2; dingus[18][12] = 2; dingus[17][12] = 2; dingus[16][12] = 2; dingus[4][13] = 2; dingus[3][13] = 2; dingus[2][13] = 2; dingus[1][13] = 2; dingus[0][13] = 2; dingus[0][12] = 2; dingus[1][12] = 2; dingus[2][12] = 2; dingus[3][12] = 2; dingus[15][13] = 2; dingus[16][13] = 2; dingus[17][13] = 2; dingus[18][13] = 2; dingus[19][13] = 2; dingus[19][14] = 2; dingus[18][14] = 2; dingus[17][14] = 2; dingus[16][14] = 2; dingus[15][14] = 2; dingus[14][14] = 2; dingus[5][14] = 2; dingus[4][14] = 2; dingus[3][14] = 2; dingus[2][14] = 2; dingus[1][14] = 2; dingus[0][14] = 2; dingus[0][15] = 2; dingus[1][15] = 2; dingus[2][15] = 2; dingus[3][15] = 2; dingus[4][15] = 2; dingus[5][15] = 2; dingus[6][15] = 2; dingus[13][15] = 2; dingus[14][15] = 2; dingus[15][15] = 2; dingus[16][15] = 2; dingus[17][15] = 2; dingus[18][15] = 2; dingus[19][15] = 2; dingus[19][16] = 2; dingus[18][16] = 2; dingus[17][16] = 2; dingus[16][16] = 2; dingus[15][16] = 2; dingus[14][16] = 2; dingus[13][16] = 2; dingus[12][16] = 2; dingus[7][16] = 2; dingus[6][16] = 2; dingus[5][16] = 2; dingus[4][16] = 2; dingus[3][16] = 2; dingus[2][16] = 2; dingus[1][16] = 2; dingus[0][16] = 2; dingus[0][17] = 2; dingus[1][17] = 2; dingus[2][17] = 2; dingus[3][17] = 2; dingus[4][17] = 2; dingus[5][17] = 2; dingus[6][17] = 2; dingus[7][17] = 2; dingus[8][17] = 2; dingus[11][17] = 2; dingus[12][17] = 2; dingus[13][17] = 2; dingus[14][17] = 2; dingus[15][17] = 2; dingus[16][17] = 2; dingus[17][17] = 2; dingus[18][17] = 2; dingus[19][17] = 2; dingus[19][18] = 2; dingus[18][18] = 2; dingus[17][18] = 2; dingus[16][18] = 2; dingus[15][18] = 2; dingus[14][18] = 2; dingus[13][18] = 2; dingus[12][18] = 2; dingus[11][18] = 2; dingus[10][18] = 2; dingus[9][18] = 2; dingus[8][18] = 2; dingus[7][18] = 2; dingus[6][18] = 2; dingus[5][18] = 2; dingus[4][18] = 2; dingus[3][18] = 2; dingus[2][18] = 2; dingus[1][18] = 2; dingus[0][18] = 2; dingus[0][19] = 2; dingus[1][19] = 2; dingus[2][19] = 2; dingus[3][19] = 2; dingus[4][19] = 2; dingus[5][19] = 2; dingus[6][19] = 2; dingus[7][19] = 2; dingus[8][19] = 2; dingus[9][19] = 2; dingus[10][19] = 2; dingus[11][19] = 2; dingus[12][19] = 2; dingus[13][19] = 2; dingus[14][19] = 2; dingus[15][19] = 2; dingus[16][19] = 2; dingus[17][19] = 2; dingus[18][19] = 2; dingus[19][19] = 2; dingus[0][0] = 2; 
		
		return new MiniMaze(14, 8, dingus);
	}
	
	public void step(){
		a += 1 + add;
		
		if (!myMaze.win && Scene.collision(this, x, y, Global.PLAYER)){
			if (!lastTouch){
				Fg.me.shakeTimer = 5;
				lastTouch = true;
				color = new int[][]{Calc.getColorHex("D578FF"), Calc.getColorHex("58C8FF"), Calc.getColorHex("C2FF78"), Calc.getColorHex("D7FB2C")};
				Sound.playPitched("sMiniMazePower", .07);
			}
			
			boolean ass = false;
			boolean moved = false;
			if (IO.checkFrameKey(Global.LEFT)){
				ass = true;
				moved = myMaze.left();
			}
			else if (IO.checkFrameKey(Global.RIGHT)){
				ass = true;
				moved = myMaze.right();
			}
			else if (IO.checkFrameKey(Global.UP)){
				ass = true;
				moved = myMaze.up();
			}
			else if (IO.checkFrameKey(Global.DOWN)){
				ass = true;
				moved = myMaze.down();
			}
			
			if (myMaze.win){
				Fg.me.shakeTimer = 10;
				Sound.play("sMiniMazeWin");
				Global.event[EV.PLANTMAZE] = 1;
				
				myMaze = heart();
				myMaze.win = true;
			}
			else if (ass){
				if (moved)
					Sound.playPitched("sMiniMazeMove");
				else
					Sound.playPitched("sMiniMazeFail");
			}
			
			add = Calc.approach(add, .2, 4);
		}
		else{
			add = Calc.approach(add, 0, 5);
			
			if (lastTouch){
				Fg.me.shakeTimer = 5;
				lastTouch = false;
				color = new int[][]{Calc.getColorHex("C2FF78"), Calc.getColorHex("58C8FF"), Calc.getColorHex("D578FF"), Calc.getColorHex("D7FB2C")};
				Sound.playPitched("sMiniMazePower", .07);
			}
		}
	}
	
	public void render(){
		int[][] data = myMaze.data();
		double xx = dX + s.x + Calc.rangedRandom(.1) - Calc.dirX(7, (a + 180) * 2.8), yy = dY + s.y + Calc.rangedRandom(.1) - Calc.dirY(7, (a + 180) * 2.8);
		
		Graphics.setAlpha(1);
		Graphics.setColor("0086D7");
		
		double line = 3.2;
		double depth = .9 + (.25 * Math.sin(a / 47.6));
		
		for (int i=0; i<26; i+=2){
			Shape.drawRectangle(xx - 2 + Calc.dirX(i * depth, a) - line, yy - 2 + Calc.dirY(i * depth, a) - line, xx + w + 2 + Calc.dirX(i * depth, a) + line, yy + h + 2 + Calc.dirY(i * depth, a) + line);
			if (i > 11 && i < 16)
				Shape.drawRectangle(xx + (w / 2) - 9 + Calc.rangedRandom(.1) + Calc.dirX(i * depth, a) - line, 0, xx + (w / 2) + 9 + Calc.rangedRandom(.1) + Calc.dirX(i * depth, a) + line, 480);
			
			line += Calc.rangedRandom(.05);
		}
		
		Graphics.setAlpha(.5);
		Graphics.setColor("00CFBC");
		for (int i=1; i<26; i+=2){
			Shape.drawRectangle(xx - 2 + Calc.dirX(i * depth, a), yy - 2 + Calc.dirY(i * depth, a), xx + w + 2 + Calc.dirX(i * depth, a), yy + h + 2 + Calc.dirY(i * depth, a));
			if (i > 11 && i < 16)
				Shape.drawRectangle(xx + (w / 2) - 9 + Calc.rangedRandom(.1) + Calc.dirX(i * depth, a), 0, xx + (w / 2) + 9 + Calc.rangedRandom(.1) + Calc.dirX(i * depth, a), 480);
		}
		
		Graphics.setAlpha(.8);
		Graphics.setColor(color[0], true);
		Shape.drawRectangle(xx - 2, yy - 2, xx + w + 2, yy + h + 2);
		
		for (int i=0; i<20; i++){
			for (int j=0; j<20; j++){
				int k = data[i][j];
				if (k > 0){
					Graphics.setAlpha(.96 + Calc.random(.4));
					Graphics.setColor(color[k], true);
					Shape.drawRectangle(xx + (i * pW), yy + (j * pH), xx + ((i + 1) * pW), yy + ((j + 1) * pH));
				}
			}
		}
		Graphics.setAlpha(1);
	}

}
