package world.event;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.particles.Sparkle;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class DassKey extends Entity{
	public Shake s;
	public double sparkleTimer;
	public boolean fall;

	public DassKey(double x, double y, boolean fall) {
		super(x, y);
		
		sprite = Sprite.get("sDassKeyDrop");
		orientation = Sprite.CENTERED;
		
		xscale = .2857;
		yscale = .2857;
		
		this.fall = fall;
		
		s = new Shake();
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-1);
		
		resetSparkle();
	}
	
	public void resetSparkle(){
		sparkleTimer = 5 + Calc.random(15);
		new Sparkle(x + Calc.rangedRandom(11), y + Calc.rangedRandom(11));
	}
	
	public void step(){
		boolean yes = true;
		
		sparkleTimer -= 1;
		if (sparkleTimer <= 0)
			resetSparkle();
		if (fall){
			vspeed += .8;
			if(Scene.collision(this, x,Math.round(y)+vspeed+1, Global.SOLIDBLOCK) && vspeed > 0){
				yes = false;
				vspeed *= -.8;
				vspeed += 1.4;

				if (vspeed > -3){
					vspeed = 0;
					fall = false;
				}
				
				Sound.playPitched("sDishClang");
				y = 112;
			}
		}
		
		if (yes) //lame
			super.step();
		
		if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 30){
			Sound.play("sDishFjank");
			Global.eventItemGet(1);
			destroy();
		}
	}
	
	public void render(){
		sprite.render(0, orientation, x + (s.x / 3.0) + Calc.rangedRandom(.4), y + (s.y / 3.0) + Calc.rangedRandom(.4), xscale, yscale, 0, 1, 1, 1, 1);
	}

}
