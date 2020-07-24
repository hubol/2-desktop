package world.event;

import graphics.Graphics;
import graphics.Shape;
import world.control.Global;
import world.control.Shake;
import main.Entity;

public class BathPie extends Entity{
	public String[] color, backColor;
	public double[] at, total;
	public Shake s;

	public BathPie(double x, double y) {
		super(x + 5, y - 2);
		color = new String[4];
		backColor = new String[4];
		
		color[0] = "FF4781";
		backColor[0] = "A82F55";
		color[1] = "44CAFF";
		backColor[1] = "2C83A5";
		color[2] = "59FF90";
		backColor[2] = "379E5A";
		color[3] = "F1FF75";
		backColor[3] = "959E48";
		
		at = new double[4];
		total = new double[4];
		
		at[0] = Global.heartsGot();
		total[0] = Global.heartAmount;
		at[1] = Global.chestsOpened() * 5;
		total[1] = Global.totalChests * 5;
		at[2] = Global.bossesKilled() * 11.25;
		total[2] = Global.totalBosses * 11.25;
		
		at[3] = (total[0] + total[1] + total[2]) - (at[0] + at[1] + at[2]);
		total[3] = at[3];
		
		s = new Shake(.3);
		setDepth(Integer.MIN_VALUE+10);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		angle += 1.0 / 360.0;
	}
	
	public void render(){
		//Shape.drawPieChart(x + 12 + s.x, y + 12 + s.y, 48, 50, angle, at, backColor[0], backColor[1], backColor[2], backColor[3]);
		Graphics.setColor("4784FF");
		Shape.drawCircle(x + s.x, y + s.y, 13);
		Shape.drawPieChart(x + s.x, y + s.y, 10, 50, angle, at, color[0], color[1], color[2], color[3]);
	}

}
