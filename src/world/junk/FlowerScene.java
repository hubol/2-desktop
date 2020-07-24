package world.junk;

import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class FlowerScene extends Entity{
	public double inSpeed, outSpeed;
	public boolean last;
	public Shake s = new Shake(.3);

	public FlowerScene(double x, double y, double ang) {
		super(x, y);
		angle = ang;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-26);
		
		if (ang == 0){
			this.x += 16;
			this.y += 32;
		}
		else if (ang == 90){
			this.x += 32;
			this.y += 16;
		}
		else if (ang == 180)
			this.x += 16;
		else if (ang == 270)
			this.y += 16;
		
		double g = ((x * 1.0689) + (y * 1.0674) + (Global.roomX * 1.3) + (Global.roomY * 1.32) + (x * y * Global.roomX * Global.roomY)) * 1.06;
		
		if (g % 2.0 < 1.0)
			sprite = Sprite.get("sFlowerScene");
		else
			sprite = Sprite.get("sFlowerSceneB");
		
		g += 6.356;
		g *= 1.1;
		
		if (g % 10 < 1)
			sprite = Sprite.get("sFlowerSceneC");
		
		g *= .98;
		xscale = (.9 + (g % .2));
		g += .9;
		if (g % 1.0 < .5)
			xscale *= -1;
		g *= .95;
		g-= .1;
		yscale = .9 + (g % .2);
		g += .13;
		g *= 1.01;
		angle += -4 + (g % 8.0);
		g *= 1.02;
		g -= .1;
		inSpeed = .3 + (g % .9);
		g += .4;
		g *= 1.03;
		outSpeed = .3 + (g % .9);
		g *= .99;
		this.x += -1 + (g % 2.0);
		g *= .98;
		this.y += -1 + (g % 2.0);
		
		inSpeed *= 2.3;
		outSpeed *= 2.9;
		
		xscale *= 1.01;
		yscale *= 1.1;
		
		orientation = Sprite.SOUTH;
		
		if (in())
			imageSingle = 14;
		else
			imageSingle = 0;
		
		last = in();
	}
	
	public boolean in(){
		return Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 64;
	}
	
	public void step(){
		final boolean in = in();
		
		if (last != in){
			if (in)
				Sound.in();
			else
				Sound.out();
		}
		
		if (in)
			imageSingle += inSpeed;
		else
			imageSingle -= outSpeed;
		
		last = in;
		
		imageSingle = Math.max(0, Math.min(14, imageSingle));
	}
	
	public void render(){
		if (imageSingle > 0){
			final double mult = .6 + (imageSingle / 35.0);
			sprite.render(imageSingle, orientation, x + s.x + Calc.rangedRandom(.02), y + s.y + Calc.rangedRandom(.02), xscale * mult, yscale * mult, angle, 1, 1, 1, 1);
		}
	}

}
