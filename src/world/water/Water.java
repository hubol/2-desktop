package world.water;

import world.control.EV;
import world.control.Global;
import graphics.Sprite;

public class Water extends WaterMom{
	public final static Sprite WATER = Sprite.get("sWater"), OUTLINE = Sprite.get("sWaterOutline");

	public Water(double x, double y) {
		super(x, y);
		orientation = Sprite.NORTHWEST;
		
		sprite = WATER;
		outline = OUTLINE;
		
		mask = Global.sBLOCK.mask;
		
		refresh();
	}
	
	public void refresh(){
		if (Global.event[EV.GOT_SNORKEL] == 0)
			setCollisionGroup(Global.SOLIDBLOCK, Global.WATERSOLID);
		else
			setCollisionGroup(Global.WATER);
	}
	
	public void step(){
		refresh();
	}
	
	public void renderFront(double x, double y, double img){
		super.renderFront(x, y, 0);
	}
	
	public void renderBack(double x, double y, double img){
		super.renderBack(x - 3, y - 3, 0);
	}

}
