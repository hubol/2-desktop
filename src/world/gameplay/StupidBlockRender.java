package world.gameplay;

import graphics.Sprite;

import java.util.ArrayList;
import java.util.Collections;

import world.control.Shake;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import main.Entity;
import main.Scene;

public class StupidBlockRender extends Entity{
	private final Shake f = new Shake(.3), b = new Shake(.3);
	private Sprite s, ss;

	public StupidBlockRender(double x, double y) {
		super(x, y);
		setDepth(-5);
		
		new SpriteLoader("sStupidBlock", "sStupidBlockOutline");
		new SoundLoader(false, "sStupidBlockKill", "sStupidBlockTone");
		s = Sprite.get("sStupidBlockOutline");
		ss = Sprite.get("sStupidBlock");
	}
	
	public void refresh(){
		ArrayList<StupidBlock> list = Scene.getEntityList(StupidBlock.class);
		for (int i=0; i<list.size(); i++)
			list.get(i).check();
	}
	
	public void render(){
		ArrayList<StupidBlock> list = Scene.getEntityList(StupidBlock.class);
		Collections.sort(list);

		for (int i=0; i<list.size(); i++)
			list.get(i).back(s, (b.x + f.x) / 2.0, (b.y + f.y) / 2.0);
		for (int i=0; i<list.size(); i++)
			list.get(i).render(ss, f.x, f.y);
	}

}
