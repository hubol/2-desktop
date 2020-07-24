package world.event;

import graphics.Sprite;
import world.control.Global;
import main.Calc;
import main.Entity;
import main.Scene;

public class TruckButterfly extends Entity{

	public TruckButterfly(double x, double y, double dir, double s) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		
		setDirSpeed(dir, s);
		
		sprite = Sprite.get("sButterfly");
		imageSingle = Calc.random(2);
		imageSpeed = .2 + Calc.random(.6);
		orientation = Sprite.NORTHEAST;
		
		mask = Sprite.get("sButterflyTrappedMask").mask;
	}
	
	public void step(){
		super.step();
		if (!Scene.collision(this, x, y, Global.TRUCKWINDOW)){
			setDirSpeed(Calc.random(360),getSpeed());
			x = xprevious;
			y = yprevious;
		}
	}
	
	public void pseudoRender(double xx, double yy){
		sprite.render(imageSingle, orientation, x + xx, y + yy, 1, 1, 0, 1, 1, 1, 1);
	}
	
	public void render(){
		
	}

}
