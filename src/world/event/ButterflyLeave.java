package world.event;

import graphics.Sprite;

import java.util.ArrayList;

import world.control.Shake;

import main.Calc;
import main.Entity;
import main.Scene;

public class ButterflyLeave extends Entity{
	public double toX, toV, toY, xProach, yProach;
	public boolean thisRoom;
	public int id;
	public Shake s;
	public double mult, sp;

	public ButterflyLeave(double x, double y, int id) {
		super(x, y);
		
		s = new Shake(1);
		
		this.id = id;
		thisRoom = false;
		
		ArrayList<ButterflyMarker> list = Scene.getEntityList(ButterflyMarker.class);
		for (int i=0; i<list.size(); i++){
			ButterflyMarker m = list.get(i);
			if (m.id == id){
				thisRoom = true;
				toX = m.x;
				toY = m.y;
			}
		}
		
		//fuck you
		if (!thisRoom){
			if (id % 2 == 0)
				toX = -16 - 64 - Calc.random(128);
			else
				toX = 656 + 64 + Calc.random(128);
			
			toV = -4-Calc.random(8);
			vspeed = -4-Calc.random(8);
		}
		
		xProach = 15 + Calc.random(3);
		yProach = 15 + Calc.random(3);
		
		sprite = Sprite.get("sButterfly");
		imageSingle = Calc.random(2);
		
		orientation = Sprite.CENTERED;
		
		alarmInitialize(1);
		imageSpeed = .2 + Calc.random(.7);
		alarm[0] = 1 + (int)Calc.random(6);
		
		mult = 8;
		sp = 4 + Calc.random(2);
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			imageSpeed = .2 + Calc.random(.7);
			alarm[0] = 1 + (int)Calc.random(6);
		}
	}
	
	public void step(){
		mult = Calc.approach(mult, 1, sp);
		
		if (!thisRoom){
			x = Calc.approach(x, toX, xProach * mult);
			vspeed = Calc.approach(vspeed, toV, yProach * mult);
			
			if (x < -20 || x > 660)
				destroy();
		}
		else{
			x = Calc.approach(x, toX, xProach * mult);
			y = Calc.approach(y, toY, yProach * mult);
			
			if (Calc.pointDistance(x, y, toX, toY) < 4){
				Butterfly i = new Butterfly(toX, toY, id);
				i.xAt = x - toX;
				i.yAt = y - toY;
				destroy();
			}
		}
		
		super.step();
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + s.x + Calc.rangedRandom(.6), y + s.y + Calc.rangedRandom(.6), 1, 1, 0, 1, 1, 1, 1);
		sprite.render(imageSingle, orientation, x + s.x + Calc.rangedRandom(.6), y + s.y + Calc.rangedRandom(.6) + 480, 1, 1, 0, 1, 1, 1, 1);
		sprite.render(imageSingle, orientation, x + s.x + Calc.rangedRandom(.6), y + s.y + Calc.rangedRandom(.6) - 480, 1, 1, 0, 1, 1, 1, 1);
	}

}
