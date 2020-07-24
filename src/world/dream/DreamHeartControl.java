package world.dream;

import java.util.ArrayList;

import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.gameplay.Heart;
import graphics.Font;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class DreamHeartControl extends Entity{
	public static DreamHeartControl me;
	public int at, timer;
	public Font myFont;
	
	public double draw, mult;
	
	public String message;

	public DreamHeartControl(double x, double y) {
		super(x, y);
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		if (me != null && !me.isDestroyed())
			me.destroy();
		
		message = "ILOVEYOU";
		
		new SoundLoader("sDreamHeartAppear");
		
		me = this;
		
		mult = .5;
		
		sprite = Sprite.get("font");
		
		setDepth(8);
		
		at = -1;
		
		draw = message.length();
		
		timer = -1;
		if (Global.event[EV.HEART_DREAM] == 0){
			draw = 0;
			timer = 60;
			ArrayList<Heart> list = Scene.getEntityList(Heart.class);
			list.get(0).destroy();
		}
		
		myFont = Global.FONT;
	}
	
	public void step(){
		if (draw < at && draw < message.length())
			draw += .0625;
		
		timer -= 1;
		if (timer == 0)
			at += 1;
		
		if (at == message.length()){
			for (int i=0; i<12; i++){
				new DreamHeartParticles(544, 224, Calc.random(360), Integer.MIN_VALUE + 10);
				DreamHeartParticles j = new DreamHeartParticles(544, 224, Calc.random(360), Integer.MIN_VALUE + 10);
				j.setSpeed(j.getSpeed() * 4);
				if (i > 3){
					j = new DreamHeartParticles(544, 224, Calc.random(360), Integer.MIN_VALUE + 10);
					j.setSpeed(j.getSpeed() * 8);
				}
			}
			Sound.playPitched("sDreamHeartLetterAppear", .09);
			Sound.play("sDreamHeartAppear");
			Global.event[EV.HEART_DREAM] = 1;
			new Heart(544, 224, 20);
			at += 1;
			
			Global.addTweet("i love you");
		}
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void render(){
		double xx = 320 - (200 * mult) + (20 * mult), yy = 224;
		for (int i=0; i<draw; i++){
			if (i == 1 || i == 5)
				xx += 40 * mult;
			
			double r = 0;
			if (draw - i < 1)
				r = 1 - (draw - i);
			double addX = Calc.rangedRandom(8) * r, addY = Calc.rangedRandom(8) * r;
			
			int img = myFont.fetchSymbolId(message.charAt(i));
			
			/*draw(xx + addX + 3, yy + addY, img, r, "0081CF");
			draw(xx + addX - 3, yy + addY, img, r, "0081CF");
			draw(xx + addX, yy + addY + 3, img, r, "0081CF");
			draw(xx + addX, yy + addY - 3, img, r, "0081CF");
			draw(xx + addX + 2, yy + addY + 2, img, r, "0081CF");
			draw(xx + addX + 2, yy + addY - 2, img, r, "0081CF");
			draw(xx + addX - 2, yy + addY + 2, img, r, "0081CF");
			draw(xx + addX - 2, yy + addY - 2, img, r, "0081CF");
			draw(xx + addX, yy + addY, img, r, (String)Calc.choose("FF2878", "FF2878", "FF2878", "FF5691", "FF44A1"));*/
			draw(xx + addX, yy + addY, img, r, "0081CF");
			xx += 40 * mult;
		}
	}
	
	public void draw(double xx, double yy, int img, double random, String color){
		sprite.render(img, Sprite.CENTERED, xx + (Math.max(.1, random) * Calc.random(5)), yy + (Math.max(.1, random) * Calc.random(5)), mult, mult,  + (Math.max(.05, random) * Calc.rangedRandom(2)), 1, color);
	}

}
