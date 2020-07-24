package world.dream;

import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.gameplay.Heart;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class HeartHolder extends Entity{
	public int hemprog, hemexis;
	public static HeartHolder me;

	public HeartHolder(double x, double y) {
		super(x, y);
		hemprog = 0;
		hemexis = 0;
		alpha = 0;
		
		me = this;
		
		if (Global.event[EV.SUNLIGHT_DREAM] > 0){
			new Heart(x, y, 21);
			destroy();
		}
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(2);
	}
	
	public void step(){
		if (hemprog > 3){
			alpha += .05;
			if (alpha >= 1){
				for (int i=0; i<12; i++){
					new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
					DreamHeartParticles j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
					j.setSpeed(j.getSpeed() * 4);
					if (i > 3){
						j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
						j.setSpeed(j.getSpeed() * 8);
					}
				}
				
				Global.event[EV.SUNLIGHT_DREAM] = 1;
				new Heart(x, y, 21);
				Sound.play("sDreamHeartAppear");
				destroy();
			}
		}
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		me = null;
		super.roomDestroy();
	}
	
	public void render(){
		for (int i=0; i<hemprog; i++)
			Sprite.get("sDreamHeartPieces").render(i, Sprite.CENTERED, x + Calc.rangedRandom(.2), y + Calc.rangedRandom(.2), 1, 1, 0, 1 - alpha, 1, 1, 1);
		Sprite.get("sHeart").render((Integer)Calc.choose(0,2,4,6), Sprite.CENTERED, x + Calc.rangedRandom(.3), y + Calc.rangedRandom(.3), 1, 1, 0, alpha * 2, 1, 1, 1);
	}

}
