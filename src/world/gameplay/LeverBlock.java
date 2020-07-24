package world.gameplay;

import graphics.Sprite;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import main.Entity;
import main.Scene;

public class LeverBlock extends Entity{
	public int checkVal;
	public boolean init, last;
	public Shake s = new Shake();

	public LeverBlock(double x, double y, int i) {
		super(x, y);
		
		if (Scene.getEntityList(LeverBlockControl.class).size() == 0)
			new LeverBlockControl();
		
		checkVal = i;
		sprite = Sprite.get("sRainLeverBlock");
		imageSpeed = 0;
		orientation = Sprite.NORTHWEST;
		
		setDepth(8);
		change();
	}
	
	public void change(){
		boolean a = Global.event[EV.RAINBLOCK] == checkVal;
		
		if (a != last || init){
			if (!init){
				//TODO particles mayb??
			}
			if (a){
				imageSingle = 1;
				setCollisionGroup(Global.SOLIDBLOCK, Global.DEACTIVATEME, Global.NOOCCUPY, Global.TEMPSOLID);
			}
			else{
				imageSingle = 0;
				setCollisionGroup(Global.DEACTIVATEME, Global.NOOCCUPY, Global.TEMPSOLID);
			}
			
			visible = !a;
		}
		
		last = a;
		init = false;
	}
	
	public void step(){
		//
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + s.x, y + s.y, 1, 1, 0, 1, 1, 1, 1);
	}

}
