package world.intro;

import world.control.Global;
import world.control.NewGen;
import graphics.Graphics;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;

public class Birthday extends Entity{
	public String text, bottomText;
	public NewGen r;
	
	public double tX=0, tY=0;
	public final int timer = 25;
	public final double move = 1;

	public Birthday(double x, double y) {
		super(x, y);
		sprite = Sprite.get("sIntro115");
		
		r = Intro.me.myGen;
		
		if (Intro.me.myGen.random(1) > .5)
			sprite = Sprite.get("sIntroBlind");
		
		text = ((String)r.choose("happy birthday ","happy cakes ")+Intro.me.birthday+(String)r.choose("!","!!","!!!")).toUpperCase();
		bottomText = ((String)r.choose("this game is for you!", "i love you!")+(String)r.choose("","!","!!")).toUpperCase();
		
		imageSpeed = 6.0 / 30.0;
		
		alarmInitialize(3);
		alarm[0] = 1 + (int)Calc.random(timer);
		alarm[1] = 1 + (int)Calc.random(timer);
		alarm[2] = 90;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			x = Calc.approach(x, xstart, 1.5);
			y = Calc.approach(y, ystart, 1.5);
			alarm[0] = 1 + (int)Calc.random(timer);
		}
		else if (i == 1){
			tX = Calc.approach(tX, 0, 1.5);
			tY = Calc.approach(tY, 0, 1.5);
			alarm[1] = 1 + (int)Calc.random(timer);
		}
		else if (i == 2){
			Intro.me.complete();
		}
	}
	
	public void step(){
		x += Calc.rangedRandom(move);
		y += Calc.rangedRandom(move);
		
		tX += Calc.rangedRandom(move);
		tY += Calc.rangedRandom(move);
		
		super.step();
	}
	
	public void render(){
		sprite.render(imageSingle, Sprite.CENTERED, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4), 1, 1, 0, 1, Intro.me.LINE);
		Graphics.setColor(Intro.me.LINE);
		Text.setFont(Global.FONT);
		Text.orientation = Text.CENTERED;
		double sc = .7;
		Text.randomize(.125);
		Text.drawTextExt(x + tX + Calc.rangedRandom(.4) + (16.0 * sc), 40 + tY + Calc.rangedRandom(.4) + (16.0 * sc), text, sc, sc, 0);
		Text.drawTextExt(x - tX + Calc.rangedRandom(.4) + (16.0 * sc), 440 - tY + Calc.rangedRandom(.4) + (16.0 * sc), bottomText, sc, sc, 0);
		Text.randomize(0);
	}

}
