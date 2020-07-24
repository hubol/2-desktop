package world.boss.slick;

import audio.Audio;
import graphics.Sprite;
import world.control.Global;
import world.control.Sound;
import main.Calc;
import main.Entity;

public class SlickBroken extends Entity{
	public int kind; //0 = BODY, 1 = MANDIBLE, 2,3 = FOOT
	
	public boolean go, play;
	public double toX, toY, toAngle, vsp, addY;
	
	public double fjaak;

	public SlickBroken(double x, double y, double a, int kind) {
		super(x, y);
		this.kind = kind;
		angle = a;
		go = false;
		fjaak = 20;
		
		play = false;
		
		vsp = 0;
		addY = 0;
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		if (go){
			addY += vsp;
			if (addY > 0){
				vsp = 0;
				addY = 0;
			}
			
			x = Calc.approach(x, toX, fjaak);
			y = Calc.approach(y, toY + addY, fjaak);
			angle = Calc.approach(angle, toAngle, fjaak);
			
			if (addY != 0)
				vsp += .8;
			
			if (!play && Calc.pointDistance(x, y, toX, toY) < 3 && Math.abs(angle - toAngle) < 3){
				play = true;
				Audio.get("sSlickAttach").setPitch(SlickBrokenControl.me.pitch);
				Sound.play("sSlickAttach");
				SlickBrokenControl.me.pitch += .09;
			}
			
			if (fjaak > 4)
				fjaak -= .5;
		}
	}
	
	public void go(double x, double y, double a, double v){
		go = true;
		toX = x;
		toY = y;
		toAngle = a;
		
		vsp = v;
	}
	
	public void render(){
		double xx = Calc.rangedRandom(.45), yy = Calc.rangedRandom(.45);
		if (kind == 0){
			Sprite.get("sSlickBody").render(0, orientation, x + xx, y + yy, 1, 1, angle, 1, 1, 1, 1);
			Sprite.get("sSlickBody").render(1, orientation, x + xx, y + yy, 1, 1, angle, 1, 1, 1, 1);
		}
		else if (kind == 1)
			Sprite.get("sSlickMandible").render(0, Sprite.SOUTH, x + xx, y + yy, 1, 1, angle, 1, 1, 1, 1);
		else if (kind ==2 || kind == 3)
			Sprite.get("sSlickFoot").render(0, Sprite.SOUTH, x + xx, y + yy, -1 + ((3 - kind) * 2), 1, angle, 1, 1, 1, 1);
		else if (kind == 4)
			Sprite.get("sSlickMandible").render(0, Sprite.SOUTH, x + xx, y + yy, -1, 1, angle, 1, 1, 1, 1);
	}

}
