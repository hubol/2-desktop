package world.junk;

import graphics.Sprite;

import java.awt.Point;

import world.control.Global;
import world.control.Shake;

import main.Calc;
import main.Entity;

public class FollowerLimb extends Entity{
	private Follower mom;
	private double dir;
	
	private final double spd = 4;
	private final double aprch = 1.5;
	private final double dist = 40;
	
	private Shake s = new Shake();

	public FollowerLimb(Follower mommy, double a) {
		super(0, 0);
		mom = mommy;
		dir = a;
		
		setDepth(26);
		setCollisionGroup(Global.DEACTIVATEME);
		
		sprite = Sprite.get("sFollowerLimb");
		
		xscale = .75;
		yscale = .75;
		
		x = getDesired().x;
		y = getDesired().y;
	}
	
	public Point getDesired(){
		return new Point((int)(mom.x + Calc.dirX(dist, dir)), (int)(mom.y + Calc.dirY(dist, dir)));
	}
	
	public void step(){
		dir += spd;
		while (dir >= 360)
			dir -= 360;
		
		angle = dir;
		
		x = Calc.approach(x, getDesired().x, aprch);
		y = Calc.approach(y, getDesired().y, aprch);
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + Calc.rangedRandom(.05) + s.x, y + Calc.rangedRandom(.05) + s.y, xscale, yscale, angle, 1, 1, 1, 1);
	}

}
