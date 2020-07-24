package world.dream;

import graphics.Sprite;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class DreamShatter extends Entity{
	public Shake s;
	public double a, div;

	public DreamShatter() {
		super(320, 240);
		
		s = new Shake(1.1);
		alarmInitialize(1);
		
		sprite = Sprite.get("sDreamShatter");
		
		div = 24 + Calc.random(12);
		a = Calc.random(div);
		angle = Calc.random(360);
		alarm[0] = 1 + (int)Calc.random(30);
	}
	
	public void alarmEvent(int i){
		div = 24 + Calc.random(12);
		a = Calc.random(div);
		angle = Calc.random(360);
		alarm[0] = 1 + (int)Calc.random(30);
	}
	
	public void step(){
		a += 1;
		alarmStep();
		angle += Calc.rangedRandom(.2);
	}
	
	public void render(){
		sprite.render(0, Sprite.CENTERED, x + s.x, y + s.y, 1, 1, angle, 1, 1, 1, 1);
		sprite.render(1, Sprite.CENTERED, x + s.x, y + s.y, 1, 1, angle, Math.abs(Math.sin(a / div)), 1, 1, 1);
	}

}
