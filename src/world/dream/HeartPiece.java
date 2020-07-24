package world.dream;

import world.control.Global;
import world.control.Sound;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class HeartPiece extends Entity{
	public int timer;
	public double sub;

	public HeartPiece(double x, double y) {
		super(x, y);
		
		orientation = Sprite.CENTERED;
		sprite = Sprite.get("sDreamHeartPieces");
		imageSingle = HeartHolder.me.hemexis;
		imageSpeed = 0;
		HeartHolder.me.hemexis += 1;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE + 10);
		
		Sound.playPitched("sSundHeartUp");
		vspeed = -8;
		timer = 15;
		
		sub = 0;
	}
	
	public void step(){
		vspeed *= .9;
		super.step();
		
		timer -= 1;
		if (timer <= 0){
			if (vspeed != 0)
				Sound.playPitched("sSundHeartGo");
			
			if (sub < 8.75)
				sub += .75;
			
			vspeed = 0;
			x = Calc.approach(x, HeartHolder.me.x, 10 + (imageSingle * 3) - sub);
			y = Calc.approach(y, HeartHolder.me.y, 10 + (imageSingle * 3) - sub);
			if (Calc.pointDistance(x, y, HeartHolder.me.x, HeartHolder.me.y) <= .5){
				Sound.playPitched("sSundHeartAttach");
				HeartHolder.me.hemprog += 1;
				destroy();
			}
		}
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4), 1, 1, 0, 1, 1, 1, 1);
	}

}
