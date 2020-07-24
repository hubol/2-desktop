package world.junk;

import world.control.Global;
import world.control.Shake;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class HiddenDass extends Entity{
	public boolean done = false;
	public Shake s = new Shake(.5);

	public HiddenDass(double x, double y, double ang) {
		super(x, y);
		imageSpeed = .5;
		orientation = Sprite.SOUTH;
		
		angle = ang;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(20);
	}
	
	public void render(){
		if (!done){
			sprite = Sprite.get("sDassHidden");
			done = true;
		}
		
		if (done)
			sprite.render(imageSingle, orientation, x + s.x + Calc.rangedRandom(.2), y + s.y + Calc.rangedRandom(.2), 1, 1, angle, 1, 1, 1, 1);
	}

}
