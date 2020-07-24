package world.boss.skull;

import world.control.Global;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class SkullShatter extends Entity{
	public Skull mother;
	public boolean phase;
	public double approach;

	public SkullShatter(double x, double y, Skull mom, int image, double ang) {
		super(x, y);
		mother = mom;
		
		approach = 15 + Player.me.random(10);
		
		imageSingle = image;
		imageSpeed = 0;
		angle = ang;
		
		phase = false;
		
		sprite = Sprite.get("sSkullShatter");
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		setDirSpeed(90 + Player.me.rangedRandom(70), 3 + Player.me.random(3));
	}
	
	public void step(){
		if (!phase)
			vspeed += .8;
		super.step();
		
		if (!phase){
			if (y > 416 && vspeed >= 0){
				vspeed = 0;
				y = 416;
				hspeed *= .7;
			}
			
			if (vspeed != 0)
				angle += vspeed * Math.signum(hspeed);
			 
			if (mother.phase == 7)
				phase = true;
		}
		else{
			x = Calc.approach(x, 320, approach);
			y = Calc.approach(y, 160, approach);
			angle = Calc.approach(angle, 0, approach);
			
			if (approach > 3)
				approach -= .39;
		}
	}

}
