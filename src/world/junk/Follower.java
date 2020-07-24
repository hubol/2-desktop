package world.junk;

import graphics.Sprite;

import java.awt.Point;

import world.control.Global;
import world.control.Shake;
import world.control.SpriteLoader;
import world.player.Player;

import main.Calc;
import main.Entity;

public class Follower extends Entity{
	private final int amt = 10;
	private final double dir = (360.0 / amt);
	
	private Shake s = new Shake();
	
	public Follower(double x, double y) {
		super(x, y);
		this.x = getDesired().x;
		this.y = getDesired().y;
		new SpriteLoader("sFollowerBody", "sFollowerLimb");
		
		for (int i=0; i<amt; i++)
			new FollowerLimb(this, dir * i);
		
		xscale = .9;
		yscale = .9;
		
		sprite = Sprite.get("sFollowerBody");
		mask = Global.sBLOCK.mask;
		
		setDepth(25);
		setCollisionGroup(Global.DEACTIVATEME, Global.PLAYER);
	}
	
	public void step(){
		x = Calc.approach(x, getDesired().x, 2);
		y = Calc.approach(y, getDesired().y, 2);
		
		angle = x;
	}
	
	public Point getDesired(){
		return new Point((int)Player.me.x, 480 - (int)Player.me.y - 96);
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + Calc.rangedRandom(.05) + s.x, y + Calc.rangedRandom(.05) + s.y, xscale, yscale, angle, 1, 1, 1, 1);
	}

}
