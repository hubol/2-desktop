package world.event;

import graphics.Sprite;
import world.control.Global;
import world.control.Sound;
import main.Calc;
import main.Entity;

public class EpicWeedJoke extends Entity{
	public int times, timer;

	public EpicWeedJoke(double x, double y) {
		super(x, y);
		
		sprite = Sprite.get("sWeed");
		orientation = Sprite.CENTERED;
		Sound.play("sEpicWeedJoke");
		setCollisionGroup(Global.DEACTIVATEME);
		
		adjust();
		times = 0;
		timer = 0;
	}
	
	public void adjust(){
		x = Calc.random(640);
		y = Calc.random(480);
		angle = Calc.random(360);
	}
	
	public void step(){
		timer += 1;
		if (timer > 3){
			adjust();
			times += 1;
		}
		if (times > 10)
			destroy();
	}
	
	public void render(){
		sprite.render(1, orientation, x + Calc.rangedRandom(.6), y + Calc.rangedRandom(.6), 1, 1, angle + Calc.rangedRandom(.7), 1, 1, 1, 1);
		sprite.render(0, orientation, x + Calc.rangedRandom(.6), y + Calc.rangedRandom(.6), 1, 1, angle + Calc.rangedRandom(.7), 1, 1, 1, 1);
	}

}
