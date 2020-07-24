package world.boss.skull;

import world.control.Global;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class Sprout extends Entity{
	public Skull mother;
	public SproutFlower child;
	public boolean spawned;

	public Sprout(double x, Skull mom) {
		super(x, 416);
		y = 416;
		
		mother = mom;
		
		spawned = false;
		child = null;
		
		setDepth(-5);
		
		sprite = Sprite.get("sSkullSprout");
		orientation = Sprite.SOUTH;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		imageSingle = 0;
		imageSpeed = .2;
		
		xscale = (Double)Calc.choose(1.0, -1.0);
	}
	
	public void step(){
		if (imageSingle >= 13 - imageSpeed){
			imageSingle = 12.9;
			imageSpeed = 0;
			
			if (!spawned){
				spawned = true;
				child = new SproutFlower(x, y - 32, 70 + (30 * mother.invHealthPercent()), mother);
			}
		}
		
		super.step();
		
		if (imageSpeed != 0){
			imageSpeed += .02;
			imageSpeed *= 1.01;
		}
		
		if (spawned && child.isDestroyed())
			destroy();
	}
	
	public void render(){
		double mult  = imageSingle / 13.0;
		if (spawned)
			mult = 0;
		
		sprite.render(imageSingle, orientation, x + (Calc.rangedRandom(8) * mult), y, 1, 1, 0, 1, 1, 1, 1);
	}

}
