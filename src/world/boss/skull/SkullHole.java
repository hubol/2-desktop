package world.boss.skull;

import graphics.Sprite;
import world.control.Global;
import main.Calc;
import main.Entity;

public class SkullHole extends Entity{
	public boolean special, fadeIn;
	public double aniTimer;
	
	public double div;
	public final double speed = 8;

	public SkullHole(double x, double y, boolean s) {
		super(x, y);
		setDepth( 5 );
		setCollisionGroup(Global.DEACTIVATEME);
		sprite = Sprite.get("sSkullHole");
		
		special = s;
		
		imageSingle = Calc.random(4);
		//angle = Calc.random(360);
		aniTimer = Calc.random(speed);
		
		fadeIn = true;
		
		if (special)
			setColor(1, .6, .8);
		
		xscale = 0;
		yscale = 2;
		
		div = 6;
	}
	
	public void step(){
		if (fadeIn){
			xscale += .007;
			xscale = Calc.approach(xscale,1.1,div);
			yscale = Calc.approach(yscale, 1, div * .5);
			
			div += .96;
			if (div > 14)
				div = 14;
			
			if (xscale >= 1){
				fadeIn = false;
				//xscale = 1.2;
				
				div = 12;
			}
		}
		else{
			xscale = Calc.approach(xscale, 0, div);
			yscale = Calc.approach(yscale, 0, Math.max(1.5, div * .97));
			xscale -= .013;
			if (div >= 1.7)
			div -= .7;
			if (xscale <= 0){
				destroy();
			}
		}
		
		aniTimer -= 1;
		if (aniTimer <= 0){
			aniTimer = speed;
			imageSingle += 1;
			if (imageSingle >= 4)
				imageSingle -= 4;
			//angle = Calc.random(360);
		}
		
		//yscale = xscale;
	}

}
