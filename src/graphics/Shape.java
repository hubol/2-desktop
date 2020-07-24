package graphics;

import javax.media.opengl.GL2;

import main.Calc;
import main.Scene;

public class Shape {
public static Sprite rect=Sprite.get("rectangle");

	public static void drawRectangle(double x1, double y1, double x2, double y2){
		rect.render(0, Sprite.NORTHWEST, x1, y1, Math.abs(x2-x1)/16, Math.abs(y2-y1)/16, 0, Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
	}
	
	public static void drawRectangle(boolean width, double x, double y, double w, double h){
		drawRectangle(x, y, x + w, y + h);
	}
	
	public static void drawLine(double x1, double y1, double x2, double y2, double width){
		rect.render(0, Sprite.WEST, x1, y1, Calc.pointDistance(x1, y1, x2, y2)/16, width/16, Calc.pointDirection(x1, y1, x2, y2), Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
	}
	
	public static void drawPoint(double x, double y){
		rect.render(0, Sprite.CENTERED, x, y, 1/16.0, 1/16.0, 0, Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
	}
	
	public static void drawCircle(double x, double y, double radius){
		drawCircle(x, y, radius, 20);
	}
	
	public static void drawCircle(double x, double y, double radius, int steps){
		Scene.gl.glColor4d(Graphics.color[0], Graphics.color[1], Graphics.color[2], Graphics.alpha);
		Scene.gl.glBegin(GL2.GL_TRIANGLE_FAN);
		Scene.gl.glVertex2d(x,y);
		for(int a = 0; a<=steps; a++){
			Scene.gl.glVertex2d(x+radius*Math.cos(Math.PI*2*(double)a/steps),y+radius*Math.sin(Math.PI*2*(double)a/steps));
			Scene.trianglesDrawn += 1;
		}
		Scene.gl.glEnd();
	}
	
	public static void drawPieChart(double x, double y, double radius, int steps, double angle, double[] values, String... colors){
		double[] percent = new double[values.length];
		double total = 0;
		for (int i=0; i<values.length; i++)
			total += values[i];
		for (int i=0; i<values.length; i++)
			percent[i] = values[i] / total;
		
		double[] stored = new double[3];
		stored = Graphics.color;
		
		for (int i=0; i<values.length; i++){
			Graphics.setColor(colors[i]);
			Scene.gl.glColor4d(Graphics.color[0], Graphics.color[1], Graphics.color[2], Graphics.alpha);
			Scene.gl.glBegin(GL2.GL_TRIANGLE_FAN);
			Scene.gl.glVertex2d(x,y);
			int tot = (int)Math.max(6, Math.ceil(steps * percent[i]));
			for(int a = 0; a<=tot; a++){
				Scene.gl.glVertex2d(x+radius*Math.cos(Math.PI*angle),y+radius*Math.sin(Math.PI*angle));
				if (a < tot)
					angle += ((1.0 / (double)tot) * percent[i]) * 2.0;
				Scene.trianglesDrawn += 1;
			}
			Scene.gl.glEnd();
		}
		
		Graphics.setColor(stored);
	}
	
	public static void drawRingChart(double x, double y, double radius, int steps, double angle, double[] values, String... colors){
		int amnt = values.length;
		double[] percent = new double[amnt];
		double total = 0;
		for (int i=0; i<amnt; i++)
			total += values[i];
		for (int i=0; i<amnt; i++)
			percent[i] = values[i] / total;
		
		double[] stored = new double[3];
		stored = Graphics.color;
		
		double totalpercent = 1;
		for (int i=amnt - 1; i>=0; i--){
			Graphics.setColor(colors[i]);
			Shape.drawCircle(x, y, Math.sqrt(totalpercent * Math.pow(radius, 2) * Math.PI) / Math.PI, (int)Math.ceil((double)steps / (double)amnt));
			totalpercent -= percent[i];
		}
		
		Graphics.setColor(stored);
	}
}
