package world.gameplay;

import java.util.ArrayList;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import main.Entity;
import main.Scene;

public class LeverBlockControl extends Entity{
	public Shake s = new Shake(.5);

	public LeverBlockControl() {
		super(0, 0);

		sprite = Sprite.get("sWaterOutline");
		
		setDepth(-12);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		
	}
	
	public void render(){
		ArrayList<LeverBlock> list = Scene.getEntityList(LeverBlock.class);
		for (int i=0; i<list.size(); i++){
			if (!list.get(i).visible)
				sprite.render(0, Sprite.NORTHWEST, list.get(i).x + s.x - 3, list.get(i).y + s.y - 3, 1, 1, 0, 1, "#00A761");
		}
		for (int i=0; i<list.size(); i++){
			if (!list.get(i).visible)
				list.get(i).sprite.render(1, Sprite.NORTHWEST, list.get(i).x + s.x, list.get(i).y + s.y, 1, 1, 0, 1, 1, 1, 1);
		}
	}

}
