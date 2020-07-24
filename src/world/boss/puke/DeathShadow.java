package world.boss.puke;

import graphics.Graphics;
import graphics.Shape;
import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class DeathShadow extends Entity{
	public double width = 0, height = 0, wSpeed, hSpeed, dW, dH, drawWidth = 0, drawHeight = 0;
	public Shake s = new Shake(.6);

	public DeathShadow(double x, double y, double a, double b) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(20);
		
		double w = Math.abs(a - x), h = Math.abs(b - y);
		
		dW = w;
		dH = h;
		
		wSpeed = w / 10.0;
		hSpeed = h / 10.0;
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void step(){
		if (width == dW && height == dH){
			
		}
		else{
			width += wSpeed;
			height += hSpeed;
			
			if (width > dW)
				width = dW;
			if (height > dH)
				height = dH;
		}
		
		drawWidth = Calc.approach(drawWidth, width, 2);
		drawHeight = Calc.approach(drawHeight, height, 2);
	}
	
	public void render(){
		Graphics.setAlpha(1);
		Graphics.setColor("0095EF");
		rect(x + Calc.rangedRandom(.15) + s.x, y + Calc.rangedRandom(.15) + s.y, x + drawWidth + Calc.rangedRandom(.15) + s.x, y + drawHeight + Calc.rangedRandom(.15) + s.y);
	}
	
	public void rect(double x, double y, double a, double b){
		Shape.drawLine(x, y, a, y, 4);
		Shape.drawLine(x, b, a, b, 4);
		Shape.drawLine(x, y, x, b, 4);
		Shape.drawLine(a, y, a, b, 4);
	}

}
