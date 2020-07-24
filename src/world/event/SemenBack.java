package world.event;

import graphics.Sprite;

import java.util.ArrayList;

import main.Calc;
import main.Entity;
import main.Scene;

public class SemenBack extends Entity{

	public SemenBack(double x, double y) {
		super(x, y);
		setDepth(-3);
		
		if (Sprite.exists("sSemenBack"))
			sprite = Sprite.get("sSemenBack");
	}
	
	public void step(){
		if (Sprite.exists("sSemenBack"))
			sprite = Sprite.get("sSemenBack");
	}
	
	public void render(){
		ArrayList<Semen> list = Scene.getEntityList(Semen.class);
		for (int i=0; i<list.size(); i++){
			Semen me = list.get(i);
			if (Sprite.exists("sSemenBack"))
				sprite.render(me.imageSingle, Sprite.CENTERED, me.x + Calc.rangedRandom(.4), me.y + Calc.rangedRandom(.4), me.xscale * me.mult, me.yscale * me.mult, me.angle, me.alpha, 1, 1, 1);
		}
	}

}
