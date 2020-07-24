package world.intro;

import world.control.Global;
import world.control.Sound;
import graphics.Font;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class DassRunner extends Entity{
	public double multiply;
	
	public final String txt = "# www.hubolhubolhubol.com # www.hubolhubolhubol.com ".toUpperCase();
	public final double r = 206;
	public final double scale = ((2.0 * Math.PI * r) / (double)txt.length()) / 32.0;
	
	public double image;
	
	public Font myFont;
	public String step;
	
	public boolean charOnly;

	public DassRunner() {
		super(320, 240);
		
		sprite = Sprite.get("sIntro115Run");
		
		multiply = 1;
		if (Intro.me.myGen.random(1) < .5)
			multiply = -1;
		
		charOnly = (Intro.me.myGen.random(1) < .5);
		
		step = (String)Intro.me.myGen.choose("sStep","sStep2","sStepError","sStepField","sStepDicks","sStepLoop","sStepLove","sStepWacky");
		
		myFont = Global.FONT;
		
		alarmInitialize(1);
		alarm[0] = 120;
	}
	
	public void step(){
		angle += multiply * .5;
		
		image += 1.0 / 18.0;
		while (image >= 2){
			Sound.playPitched(step, .069);
			image -= 2;
		}
		
		alarmStep();
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			Intro.me.complete();
		}
	}
	
	public void render(){
		//planet
		double fck = angle;
		if (charOnly)
			fck = 0;
		Sprite.get("sIntroDassPlanet").render(0, Sprite.CENTERED, x + Calc.rangedRandom(.7), y + Calc.rangedRandom(.7), 1, 1, fck, 1, Intro.me.LINE);
		//115
		double addY = 2;
		if ((int)image == 1)
			addY = -1;
		
		double xx = x, yy = y - 150 + addY, dir = 0;
		if (charOnly){
			xx = x + Calc.dirX(150 - addY, -angle + 90);
			yy = y + Calc.dirY(150 - addY, -angle + 90);
			dir = -angle;
		}
		
		sprite.render((int)image + 2, Sprite.CENTERED, xx + Calc.rangedRandom(.7), yy + Calc.rangedRandom(.7), multiply, 1, dir, 1, Intro.me.BACK);
		sprite.render((int)image, Sprite.CENTERED, xx + Calc.rangedRandom(.7), yy + Calc.rangedRandom(.7), multiply, 1, dir, 1, Intro.me.LINE);
		//text in outer space
		final double add = 360.0 / (double)txt.length();
		double ang = fck;
		for (int i=0; i<txt.length(); i++){
			char c = txt.charAt(i); 
			if (c != ' '){
				Sprite.get("font").render(myFont.fetchSymbolId(c), Sprite.CENTERED, x + Calc.dirX(r, ang) + Calc.rangedRandom(.6), y + Calc.dirY(r, ang) + Calc.rangedRandom(.6), scale, scale, ang + 270, 1, Intro.me.LINE);
			}
			ang -= add;
		}
	}

}
