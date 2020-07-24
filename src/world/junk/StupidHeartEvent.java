package world.junk;

import java.util.ArrayList;

import graphics.Graphics;
import graphics.Shape;
import audio.Audio;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.gameplay.StupidBlock;
import main.Entity;
import main.Scene;

public class StupidHeartEvent extends Entity{
	private Shake s = new Shake(.3);
	private final double max = Scene.getEntityList(StupidBlock.class).size();
	private double completion = 0;
	
	private boolean win = false;

	public StupidHeartEvent(double x, double y) {
		super(x, y);
		if (Global.event[EV.STUPIDHEART] == 1)
			destroy();
		
		setDepth(30);
	}
	
	public void update(){
		int amt = 0;
		ArrayList<StupidBlock> block = Scene.getEntityList(StupidBlock.class);
		for (int i=0; i<block.size(); i++){
			if (!block.get(i).killed)
				amt++;
		}
		completion = 1 - ((double)amt / max);
		Audio.get("sStupidBlockTone").setPitch(1 + completion);
		Sound.play("sStupidBlockTone");
		
		if (completion >= 1){
			win = true;
			Global.event[EV.STUPIDHEART] = 1;
		}
	}
	
	public void step(){
		if (win)
			xscale *= .85;
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void render(){
		Graphics.setColor("FF1879");
		Shape.drawCircle(x + s.x, y + s.y, 18.5 * xscale);
		Shape.drawPieChart(x + s.x, y + s.y, 16 * xscale, 20, 0, new double[]{completion, 1 - completion}, "D7FF18", "FF1879");
	}

}
