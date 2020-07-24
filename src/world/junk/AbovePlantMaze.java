package world.junk;

import graphics.Graphics;
import graphics.Shape;
import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class AbovePlantMaze extends Entity{
	public final double dX = 448, w = 128;
	
	public double a = 0;
	public Shake s = new Shake(.7);

	public AbovePlantMaze(double x, double y) {
		super(x, y + 3);
		
		a = Global.storedA;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE + 10);
	}
	
	public void destroy(){
		Global.storedA = a;
		super.destroy();
	}
	
	public void roomDestroy(){
		Global.storedA = a;
		super.roomDestroy();
	}
	
	public void step(){
		a += 1;
	}
	
	public void render(){
		double xx = dX + s.x + Calc.rangedRandom(.1) - Calc.dirX(7, (a + 180) * 2.8);
		
		Graphics.setAlpha(1);
		Graphics.setColor("0086D7");
		
		double line = 3.2;
		double depth = .9 + (.25 * Math.sin(a / 47.6));
		
		for (int i=0; i<26; i+=2){
			if (i > 11 && i < 17)
				Shape.drawRectangle(xx + (w / 2) - 9 + Calc.rangedRandom(.1) + Calc.dirX(i * depth, a) - line, 464 - (i * 4) + Calc.dirY(i * depth, a) - line, xx + (w / 2) + 9 + Calc.rangedRandom(.1) + Calc.dirX(i * depth, a) + line, 480);
			
			line += Calc.rangedRandom(.05);
		}
		
		Graphics.setAlpha(.25);
		Graphics.setColor("00CFBC");
		for (double i=1; i<26; i+=1){
			if (i > 11 && i < 16)
				Shape.drawRectangle(xx + (w / 2) - 9 + Calc.rangedRandom(.1) + Calc.dirX(i * depth, a), 464 - (i * 4) + Calc.dirY(i * depth, a), xx + (w / 2) + 9 + Calc.rangedRandom(.1) + Calc.dirX(i * depth, a), 480);
		}
		
		Graphics.setAlpha(1);
	}

}
