package world.boss.jelly;

import world.control.Global;
import main.Calc;
import graphics.Sprite;

public class JellyExclamation extends Jelly{
	public double life;
	
	//isnt there a less shit way of doing this??? i literally dont remember
	public JellyExclamation(double x, double y){
		super(x, y);

		orientation = Sprite.CENTERED;
		sprite = Sprite.get("sJellyWarn");
		backSprite = Sprite.get("sJellyWarnBack");
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		imageSingle = 0;
		imageSpeed = .3;
		vspeed = -3;
	}

	public void step(){
		x += Calc.rangedRandom(.4);
		y += Calc.rangedRandom(.4);
		
		super.step();
		vspeed *= .9;
		if (vspeed >= -.02)
			destroy();
	}
	
	public void render(){
		
	}

}
