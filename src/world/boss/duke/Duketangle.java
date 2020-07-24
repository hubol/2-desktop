package world.boss.duke;

import graphics.Graphics;
import graphics.Shape;
import audio.Audio;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.player.Player;
import main.Calc;
import main.Entity;

public class Duketangle extends Entity{
	public double width = 0, height = 0, wSpeed, hSpeed, dW, dH, drawWidth = 0, drawHeight = 0;
	public boolean a = false;
	public Shake s = new Shake(.6);

	public Duketangle(double x, double y, double a, double b) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		
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
			if (inBounds(Player.me.x - 16, Player.me.y - 16) || inBounds(Player.me.x + 16, Player.me.y + 16))
				Player.hurtPlayer(150);
		}
		else{
			width += wSpeed;
			height += hSpeed;
			
			if (width > dW)
				width = dW;
			if (height > dH)
				height = dH;
			
			a = !a;
			if (a){
				Audio.get("sDuketangle").setPitch(1.0 + (width / dW));
				Sound.play("sDuketangle");
			}
		}
		
		drawWidth = Calc.approach(drawWidth, width, 2);
		drawHeight = Calc.approach(drawHeight, height, 2);
	}
	
	public boolean inBounds(double x, double y){
		return (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);
	}
	
	public void render(){
		Graphics.setAlpha(.8);
		Graphics.setColor("FAFF58");
		Shape.drawRectangle(x + Calc.rangedRandom(.15) + s.x, y + Calc.rangedRandom(.15) + s.y, x + drawWidth + Calc.rangedRandom(.15) + s.x, y + drawHeight + Calc.rangedRandom(.15) + s.y);
		Graphics.setAlpha(1);
	}

}
