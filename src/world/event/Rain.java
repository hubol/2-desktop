package world.event;

import world.control.EV;
import world.control.Global;
import graphics.Sprite;
import main.Entity;

public class Rain extends Entity{
	public boolean check;
	public int checkVal;

	public Rain(double x, double y, int i) {
		super(x, y);
		init(i);
		check = false;
		checkVal = 0;
	}
	
	public Rain(double x, double y, int i, int val) {
		super(x, y);
		init(i);
		check = true;
		checkVal = val;
		visible();
	}
	
	public void init(int i){
		if (i != 0)
			sprite = Sprite.get("sRainSplash");
		else
			sprite = Sprite.get("sRainFall");
		
		orientation = Sprite.NORTHWEST;
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-15);
		
		imageSpeed = .3;
		
	}
	
	public void visible(){
		if (check)
			visible = Global.event[EV.RAINBLOCK] == checkVal;
	}
	
	public void step(){
		visible();
		super.step();
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + Global.rain.x, y + Global.rain.y, 1, 1, 0, 1, 1, 1, 1);
	}

}
