package world.intro;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class Lightning extends Entity{

	public Lightning(double x, double y) {
		super(x, y);
		alarmInitialize(1);
		alarm[0] = 7;
		
		sprite = Sprite.get("sIntroLightning");
		vspeed = 1;
	}
	
	public void alarmEvent(int i){
		destroy();
	}
	
	public void step(){
		x += Calc.rangedRandom(1);
		y += Calc.rangedRandom(1);
		super.step();
	}
	
	public void render(){
		sprite.render(1, Sprite.NORTH, x, y, 1, 1, 0, 1, Intro.me.LINE);
		sprite.render(0, Sprite.NORTH, x, y, 1, 1, 0, 1, Intro.me.LINE);
	}

}
