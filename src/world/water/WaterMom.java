package world.water;

import world.control.Global;
import graphics.Sprite;
import main.Entity;
import main.Scene;

public class WaterMom extends Entity{
	public Sprite outline;

	public WaterMom(double x, double y) {
		super(x, y);
		
		if (Scene.getEntityList(WaterControl.class).size() == 0)
			new WaterControl();
	}
	
	public void renderBack(double x, double y, double img){
		outline.render(img, orientation, this.x + x, this.y + y, xscale, yscale, angle, 1, Global.bloodOutlineColor);
	}
	
	public void renderFront(double x, double y, double img){
		sprite.render(img, orientation, this.x + x, this.y + y, xscale, yscale, angle, 1, Global.bloodColor);
	}
	
	public void render(){
		
	}

}
