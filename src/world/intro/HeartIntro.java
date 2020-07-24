package world.intro;

import world.control.Sound;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class HeartIntro extends Entity{
	public double b=0, ax, ay;
	public int phase;
	
	public int mult;
	
	public double split, splitSpeed;
	public boolean splitten;

	public HeartIntro() {
		super(0, 0);
		sprite = Sprite.get("sIntroHubol");
		imageSpeed = 6.0 / 30.0;
		
		phase = 0;
		
		split = 0;
		splitSpeed = 0;
		splitten = false;
		
		mult = 1;
		
		alarmInitialize(3);
		alarm[0] = 60;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			//shakey
			phase += 1;
			alarm[1] = 15;
		}
		else if (i == 1){
			splitten = true;
			phase += 1;
			splitSpeed = 36;
			ax = 0;
			Sound.jarBreakPlay();
			alarm[2] = 90;
		}
		else if (i == 2){
			Intro.me.complete();
		}
	}
	
	public void step(){
		if (phase == 0){
			b += 1;
			ay = 7.7 * Math.sin(b / 8.0);
		}
		else if (phase == 1){
			Sound.playPitched("sIntroHeartShake",.1);
			ay = Calc.approach(ay, 0, 12);
			ax = 6 * mult;
			mult *= -1;
		}
		
		split += splitSpeed;
		
		if (splitSpeed > 16)
			splitSpeed = Calc.approach(splitSpeed, 20, 2.5);
		
		super.step();
	}
	
	public void render(){
		if (!splitten)
			Sprite.get("sIntroHeart").render(0, Sprite.CENTERED, 320 + ax + Calc.rangedRandom(.7), 240 + ay + Calc.rangedRandom(.7), 1, 1, 0, 1, Intro.me.LINE);
		else{
			double sp = Math.min(300, split);
			sprite.renderPart((int)imageSingle, Sprite.NORTHWEST, 20 + Calc.rangedRandom(.7), 169 + Calc.rangedRandom(.7), 300 - sp, 0, sp * 2, 142, 1, 1, 0, 1, Intro.me.LINE);
			Sprite.get("sIntroHeart").render(1, Sprite.CENTERED, 320 + ax + Calc.rangedRandom(.7) - split, 240 + ay + Calc.rangedRandom(.7), 1, 1, 0, 1, Intro.me.LINE);
			Sprite.get("sIntroHeart").render(2, Sprite.CENTERED, 320 + ax + Calc.rangedRandom(.7) + split, 240 + ay + Calc.rangedRandom(.7), 1, 1, 0, 1, Intro.me.LINE);
		}
	}

}
