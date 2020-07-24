package world.event;

import java.util.ArrayList;

import world.control.Global;
import world.control.Shake;
import world.interact.Babe;
import world.particles.Sparkle;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class DinoBoner extends Entity{
	public double a;
	public int id;
	
	public Shake s = new Shake();
	public double sparkleTimer;

	public DinoBoner(double x, double y, int i) {
		super(x, y);
		sprite = Sprite.get("sDinoBoner");
		orientation = Sprite.SOUTH;
		mask = sprite.mask;
		
		id = 3 + i;
		
		imageSingle = i;
		imageSpeed = 0;
		
		a = 0;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-1);
		
		if (Global.eventItem[id] > 0)
			destroy();
		else
			resetSparkle();
	}
	
	public void resetSparkle(){
		sparkleTimer = 3 + Calc.random(10);
		new Sparkle(x + Calc.rangedRandom(17), y + Calc.rangedRandom(17));
	}
	
	public void step(){
		a += 1;
		
		sparkleTimer -= 1;
		if (sparkleTimer <= 0)
			resetSparkle();
		
		if (Scene.collision(this, x, y, Global.PLAYER) && !Scene.collision(this, x, y, Global.BOMBROCK)){
			Global.eventItemGet(id);
			ArrayList<Babe> fuck = Scene.getEntityList(Babe.class);
			if (fuck.size() > 0)
				fuck.get(0).refresh();
			destroy();
		}
	}
	
	public void render(){
		sprite.render(imageSingle, Sprite.SOUTH, x + s.x, y + s.y + Math.sin(a / 30.0), xscale, xscale, 0, 1, 1, 1, 1);
	}

}
