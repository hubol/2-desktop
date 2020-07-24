package world.event;

import world.gameplay.Heart;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class DreamMusicHeartAppear extends Entity{

	public DreamMusicHeartAppear(double x, double y) {
		super(x, y);
		orientation = Sprite.CENTERED;
		sprite = Sprite.get("sHeart");
		
		imageSpeed = 0;
	}
	
	public void step(){
		imageSingle = (Integer)Calc.choose(0,2,4,6);
		y-=.25;
		
		if (y > ystart - 30){
			for (int i=0; i<2; i++)
				new DreamGroundParticle(x + Calc.rangedRandom(9), Math.max(416, y - 13 - Calc.random(6)));
		}
		
		if (y <= ystart - 48){
			y = ystart - 48;
			new Heart(x, y, 18);
			destroy();
		}
	}

}
