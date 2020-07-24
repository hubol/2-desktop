package world.boss.jelly;

import world.enemy.BasicEnemy;
import graphics.Sprite;
import main.Calc;

public class JellyEnemy extends BasicEnemy{
	public Sprite backSprite;

	/**this class exists for jelly boss entities to extend off so that they get their backside drawn resulting in pretty outlines!*/
	public JellyEnemy(double x, double y) {
		super(x, y);
		
		backSprite = Sprite.get("sTest");
	}
	
	public double getDrawY(){
		return y;
	}
	
	public void renderBack(){
		backSprite.render((int)imageSingle, orientation, x + Calc.rangedRandom(.6), y + Calc.rangedRandom(.6), xscale, yscale, angle, alpha, colR, colG, colB);
	}
	
	public void renderFront(){
		sprite.render((int)imageSingle, orientation, x + Calc.rangedRandom(.6), y + Calc.rangedRandom(.6), xscale, yscale, angle, alpha, colR, colG, colB);
	}

}
