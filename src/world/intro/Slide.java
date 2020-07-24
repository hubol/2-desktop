package world.intro;

import audio.Audio;
import graphics.Sprite;
import world.control.Shake;
import world.control.Sound;
import main.Calc;
import main.Entity;

public class Slide extends Entity{
	public double[] xx;
	public Shake[] shake;
	public double dec;
	
	public boolean go;
	public boolean[] imgs;
	public double img;
	
	public double mult;
	public int lastImg;
	
	public double pitch;

	public Slide() {
		super(0, 0);
		xx = new double[3];
		
		if (Intro.me.myGen.random(1) < .5){
			xx[0] = -340;
			xx[1] = 960;
			xx[2] = -340;
		}
		else{
			xx[0] = 960;
			xx[1] = -340;
			xx[2] = 960;
		}
		
		go = false;
		img = 0;
		imgs = new boolean[3];
		
		mult = 1;
		
		lastImg = 0;
		
		double a = Intro.me.myGen.random(1);
		if (a > (2.0 / 3.0)){
			imgs[0] = true;
			imgs[1] = false;
			imgs[2] = true;
		}
		else if (a > (1.0 / 3.0)){
			imgs[0] = false;
			imgs[1] = true;
			imgs[2] = false;
		}
		else{
			imgs[0] = false;
			imgs[1] = false;
			imgs[2] = false;
		}
		
		sprite = Sprite.get("sIntroHubolSlide");
		orientation = Sprite.NORTH;
		
		shake = new Shake[3];
		for (int i=0; i<3; i++)
			shake[i] = new Shake();
		
		alarmInitialize(1);
		alarm[0] = 120;
		
		dec = 0;
		
		pitch = 1;
	}
	
	public void alarmEvent(int i){
		Intro.me.complete();
	}
	
	public void step(){
		for (int i=0; i<3; i++)
			xx[i] = Calc.approach(xx[i], 320, 12 - Math.min(10, dec));
		alarmStep();
		
		mult = Calc.approach(mult, 1, 7);
		
		if (Math.abs(320 - xx[0]) < 8 && !go){
			xx[0] = 320;
			xx[1] = 320;
			xx[2] = 320;
			
			go = true;
			mult = 30;
			Sound.play("sIntroClack");
		}
		
		if (go){
			img += .35;
			img *= 1.3;
			if (img > 5)
				img = 5;
			
			if ((int)img != lastImg){
				pitch += .125;
				Audio.get("sIntroClack").setPitch(pitch);
				Sound.play("sIntroClack");
			}
			
			lastImg = (int)img;
		}
		
		dec += .4;
		dec *= 1.05;
	}
	
	public void render(){
		double yy = 48;
		for (int i=0; i<3; i++){
			int j = 0;
			if (imgs[i])
				j = (int)img;
			sprite.render(j, orientation, xx[i] + shake[i].x + Calc.rangedRandom(.5 * mult), yy + shake[i].y + Calc.rangedRandom(.5 * mult), 96.0 / 18.0, 96.0 / 18.0, 0, 1, Intro.me.LINE);
			yy += 144;
		}
	}

}
