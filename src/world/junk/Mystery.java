package world.junk;

import world.control.Global;
import graphics.BlendMode;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class Mystery extends Entity{

	public Mystery(double x, double y) {
		super(x, y);
		imageSingle = (int)Calc.random(4);
		sprite = Sprite.get("sSoundGen");
		
		setDepth(-69);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		y -= 4 * alpha;
		
		alpha = Calc.approach(alpha, 0, 4);
		alpha -= .1;
		
		if (alpha <= 0)
			destroy();
	}
	
	public void render(){
		BlendMode.MULTIPLY.set();
		for (int i=0; i<128; i++)
			sprite.render(imageSingle + (i * 4), Sprite.WEST, x - 64 + i, y - 64 + Calc.rangedRandom(.4), 1, 1, 0, alpha, 1, 1, 1);
		
		BlendMode.NORMAL.set();
	}

}
