package world.water;

import java.util.ArrayList;

import world.control.Global;
import world.control.Shake;

import main.Entity;
import main.Scene;

public class WaterControl extends Entity{
	public double image;
	public Shake s, e, p;

	public WaterControl() {
		super(0, 0);	
		image = 0;
		
		setDepth( 21 );
		setCollisionGroup(Global.DEACTIVATEME);
		
		s = new Shake(.125);
		e = new Shake(.25);
		p = new Shake(.25);
	}
	
	public void step(){
		image += .8;
		if (image >= 15)
			image -= 15;
	}
	
	public void draw(ArrayList<WaterMom> list, double x, double y){
		for (int i=0; i<list.size(); i++)
			list.get(i).renderBack(s.x + e.x + x, s.y + e.y + y, image);
	}
	
	public void render(){
		ArrayList<WaterMom> list = Scene.getEntityList(WaterMom.class);
		draw(list, 0, 0);
		draw(list, 1, 0);
		draw(list, -1, 0);
		draw(list, 0, 1);
		draw(list, 0, -1);
		
		for (int i=0; i<list.size(); i++)
			list.get(i).renderFront(s.x + p.x, s.y + p.y, image);
	}

}
