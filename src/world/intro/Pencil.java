package world.intro;

import java.util.ArrayList;

import audio.Audio;

import world.control.Shake;
import world.control.Sound;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class Pencil extends Entity{
	public ArrayList<Integer> xx, yy;
	public Shake s;
	public int at, fk;
	public String sound;
	public boolean aa;
	
	public Pencil() {
		super(0, 0);
		sprite = Sprite.get("sIntroPencil");
		
		xx = new ArrayList<Integer>();
		yy = new ArrayList<Integer>();
		
		double a = Intro.me.myGen.random(1);
		if (a > .8){
			xx.add(48); yy.add(0); xx.add(48); yy.add(32); xx.add(48); yy.add(64); xx.add(48); yy.add(32); xx.add(80); yy.add(32); xx.add(112); yy.add(32); xx.add(112); yy.add(0); xx.add(112); yy.add(32); xx.add(112); yy.add(64); xx.add(176); yy.add(0); xx.add(176); yy.add(32); xx.add(208); yy.add(64); xx.add(240); yy.add(32); xx.add(240); yy.add(0); xx.add(304); yy.add(0); xx.add(304); yy.add(32); xx.add(304); yy.add(64); xx.add(336); yy.add(32); xx.add(336); yy.add(64); xx.add(400); yy.add(32); xx.add(432); yy.add(0); xx.add(464); yy.add(32); xx.add(432); yy.add(64); xx.add(528); yy.add(0); xx.add(528); yy.add(32); xx.add(528); yy.add(64); xx.add(560); yy.add(64); xx.add(48); yy.add(128); xx.add(48); yy.add(160); xx.add(48); yy.add(192); xx.add(48); yy.add(160); xx.add(80); yy.add(160); xx.add(112); yy.add(160); xx.add(112); yy.add(128); xx.add(112); yy.add(160); xx.add(112); yy.add(192); xx.add(176); yy.add(128); xx.add(176); yy.add(160); xx.add(208); yy.add(192); xx.add(240); yy.add(160); xx.add(240); yy.add(128); xx.add(304); yy.add(128); xx.add(304); yy.add(160); xx.add(304); yy.add(192); xx.add(336); yy.add(160); xx.add(336); yy.add(192); xx.add(400); yy.add(160); xx.add(432); yy.add(128); xx.add(464); yy.add(160); xx.add(432); yy.add(192); xx.add(528); yy.add(128); xx.add(528); yy.add(160); xx.add(528); yy.add(192); xx.add(560); yy.add(192); xx.add(48); yy.add(256); xx.add(48); yy.add(288); xx.add(48); yy.add(320); xx.add(112); yy.add(256); xx.add(112); yy.add(288); xx.add(112); yy.add(320); xx.add(80); yy.add(288); xx.add(176); yy.add(256); xx.add(176); yy.add(288); xx.add(208); yy.add(320); xx.add(240); yy.add(288); xx.add(240); yy.add(256); xx.add(304); yy.add(256); xx.add(304); yy.add(288); xx.add(304); yy.add(320); xx.add(336); yy.add(288); xx.add(336); yy.add(320); xx.add(400); yy.add(288); xx.add(432); yy.add(256); xx.add(464); yy.add(288); xx.add(432); yy.add(320); xx.add(528); yy.add(256); xx.add(528); yy.add(288); xx.add(528); yy.add(320); xx.add(560); yy.add(320); xx.add(48); yy.add(448); xx.add(144); yy.add(384); xx.add(112); yy.add(416); xx.add(144); yy.add(448); xx.add(208); yy.add(416); xx.add(240); yy.add(384); xx.add(272); yy.add(416); xx.add(240); yy.add(448); xx.add(336); yy.add(384); xx.add(336); yy.add(416); xx.add(336); yy.add(448); xx.add(368); yy.add(416); xx.add(400); yy.add(416); xx.add(400); yy.add(448); xx.add(432); yy.add(416); xx.add(464); yy.add(416); xx.add(464); yy.add(448); 
		}
		else if (a > .5){
			xx.add(16); yy.add(160); xx.add(16); yy.add(192); xx.add(16); yy.add(224); xx.add(16); yy.add(256); xx.add(16); yy.add(288); xx.add(80); yy.add(160); xx.add(80); yy.add(192); xx.add(80); yy.add(224); xx.add(80); yy.add(256); xx.add(80); yy.add(288); xx.add(16); yy.add(224); xx.add(48); yy.add(224); xx.add(80); yy.add(224); xx.add(144); yy.add(160); xx.add(144); yy.add(192); xx.add(144); yy.add(224); xx.add(144); yy.add(256); xx.add(176); yy.add(288); xx.add(208); yy.add(256); xx.add(208); yy.add(224); xx.add(208); yy.add(192); xx.add(208); yy.add(160); xx.add(272); yy.add(160); xx.add(272); yy.add(192); xx.add(272); yy.add(224); xx.add(272); yy.add(256); xx.add(272); yy.add(288); xx.add(304); yy.add(160); xx.add(336); yy.add(192); xx.add(304); yy.add(224); xx.add(336); yy.add(256); xx.add(304); yy.add(288); xx.add(400); yy.add(192); xx.add(400); yy.add(224); xx.add(400); yy.add(256); xx.add(432); yy.add(288); xx.add(464); yy.add(256); xx.add(464); yy.add(224); xx.add(464); yy.add(192); xx.add(432); yy.add(160); xx.add(528); yy.add(160); xx.add(528); yy.add(192); xx.add(528); yy.add(224); xx.add(528); yy.add(256); xx.add(528); yy.add(288); xx.add(560); yy.add(288); xx.add(592); yy.add(288); 
		}
		else if (a > .25){
			xx.add(16); yy.add(160); xx.add(16); yy.add(192); xx.add(16); yy.add(224); xx.add(16); yy.add(256); xx.add(16); yy.add(288); xx.add(80); yy.add(160); xx.add(80); yy.add(192); xx.add(80); yy.add(224); xx.add(80); yy.add(256); xx.add(80); yy.add(288); xx.add(16); yy.add(224); xx.add(48); yy.add(224); xx.add(80); yy.add(224); xx.add(144); yy.add(160); xx.add(144); yy.add(192); xx.add(144); yy.add(224); xx.add(144); yy.add(256); xx.add(176); yy.add(288); xx.add(208); yy.add(256); xx.add(208); yy.add(224); xx.add(208); yy.add(192); xx.add(208); yy.add(160); xx.add(272); yy.add(160); xx.add(272); yy.add(192); xx.add(272); yy.add(224); xx.add(272); yy.add(256); xx.add(272); yy.add(288); xx.add(304); yy.add(160); xx.add(336); yy.add(192); xx.add(304); yy.add(224); xx.add(336); yy.add(256); xx.add(304); yy.add(288); xx.add(400); yy.add(192); xx.add(400); yy.add(224); xx.add(400); yy.add(256); xx.add(432); yy.add(288); xx.add(464); yy.add(256); xx.add(464); yy.add(224); xx.add(464); yy.add(192); xx.add(432); yy.add(160); xx.add(528); yy.add(160); xx.add(528); yy.add(192); xx.add(528); yy.add(224); xx.add(528); yy.add(256); xx.add(528); yy.add(288); xx.add(560); yy.add(288); xx.add(592); yy.add(288); xx.add(16); yy.add(96); xx.add(80); yy.add(96); xx.add(144); yy.add(96); xx.add(208); yy.add(96); xx.add(272); yy.add(96); xx.add(336); yy.add(96); xx.add(400); yy.add(96); xx.add(464); yy.add(96); xx.add(528); yy.add(96); xx.add(592); yy.add(96); xx.add(16); yy.add(352); xx.add(80); yy.add(352); xx.add(144); yy.add(352); xx.add(208); yy.add(352); xx.add(272); yy.add(352); xx.add(336); yy.add(352); xx.add(400); yy.add(352); xx.add(464); yy.add(352); xx.add(528); yy.add(352); xx.add(592); yy.add(352); 
		}
		else{
			xx.add(16); yy.add(160); xx.add(16); yy.add(192); xx.add(16); yy.add(224); xx.add(16); yy.add(256); xx.add(16); yy.add(288); xx.add(80); yy.add(160); xx.add(80); yy.add(192); xx.add(80); yy.add(224); xx.add(80); yy.add(256); xx.add(80); yy.add(288); xx.add(16); yy.add(224); xx.add(48); yy.add(224); xx.add(80); yy.add(224); xx.add(144); yy.add(160); xx.add(144); yy.add(192); xx.add(144); yy.add(224); xx.add(144); yy.add(256); xx.add(176); yy.add(288); xx.add(208); yy.add(256); xx.add(208); yy.add(224); xx.add(208); yy.add(192); xx.add(208); yy.add(160); xx.add(272); yy.add(160); xx.add(272); yy.add(192); xx.add(272); yy.add(224); xx.add(272); yy.add(256); xx.add(272); yy.add(288); xx.add(304); yy.add(160); xx.add(336); yy.add(192); xx.add(304); yy.add(224); xx.add(336); yy.add(256); xx.add(304); yy.add(288); xx.add(400); yy.add(192); xx.add(400); yy.add(224); xx.add(400); yy.add(256); xx.add(432); yy.add(288); xx.add(464); yy.add(256); xx.add(464); yy.add(224); xx.add(464); yy.add(192); xx.add(432); yy.add(160); xx.add(528); yy.add(160); xx.add(528); yy.add(192); xx.add(528); yy.add(224); xx.add(528); yy.add(256); xx.add(528); yy.add(288); xx.add(560); yy.add(288); xx.add(592); yy.add(288); xx.add(16); yy.add(96); xx.add(48); yy.add(96); xx.add(80); yy.add(96); xx.add(112); yy.add(96); xx.add(144); yy.add(96); xx.add(176); yy.add(96); xx.add(208); yy.add(96); xx.add(240); yy.add(96); xx.add(272); yy.add(96); xx.add(304); yy.add(96); xx.add(336); yy.add(96); xx.add(368); yy.add(96); xx.add(400); yy.add(96); xx.add(432); yy.add(96); xx.add(464); yy.add(96); xx.add(496); yy.add(96); xx.add(528); yy.add(96); xx.add(560); yy.add(96); xx.add(592); yy.add(96); xx.add(16); yy.add(352); xx.add(48); yy.add(352); xx.add(80); yy.add(352); xx.add(112); yy.add(352); xx.add(144); yy.add(352); xx.add(176); yy.add(352); xx.add(208); yy.add(352); xx.add(240); yy.add(352); xx.add(272); yy.add(352); xx.add(304); yy.add(352); xx.add(336); yy.add(352); xx.add(368); yy.add(352); xx.add(400); yy.add(352); xx.add(432); yy.add(352); xx.add(464); yy.add(352); xx.add(496); yy.add(352); xx.add(528); yy.add(352); xx.add(560); yy.add(352); xx.add(592); yy.add(352); 
		}
		
		sound = "sIntroWrite"+(int)Intro.me.myGen.random(3);
		
		s = new Shake();
		
		fk = 0;
		at = 0;
		
		x = xx.get(0);
		y = yy.get(0);
		
		alarmInitialize(2);
		alarm[0] = 1;
		
		setDepth(-1);
		
		aa = true;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			Audio.get(sound).setPitch(1 + ((double)xx.get(at) / 640.0));
			Sound.play(sound);
			new PencilDot(xx.get(at), yy.get(at), this);
			
			fk = at;
			at += 1;
			
			if (at >= xx.size()){
				visible = false;
				alarm[1] = 60;
			}
			else
				alarm[0] = 3 + Calc.boolToInt(aa);
			
			aa = !aa;
		}
		else if (i == 1){
			Intro.me.complete();
		}
	}
	
	public void step(){
		x = Calc.approach(x, xx.get(Math.min(xx.size() - 1, fk)) + 16, 1.1);
		y = Calc.approach(y, yy.get(Math.min(xx.size() - 1, fk)) + 16, 1.1);
		
		alarmStep();
	}
	
	public void render(){
		sprite.render(0, Sprite.SOUTHWEST, x + Calc.rangedRandom(.4) + s.x, y + Calc.rangedRandom(.4) + s.y, 1, 1, 0, 1, Intro.me.LINE);
		sprite.render(1, Sprite.SOUTHWEST, x + Calc.rangedRandom(.4) + s.x, y + Calc.rangedRandom(.4) + s.y, 1, 1, 0, 1, Intro.me.BACK);
	}

}
