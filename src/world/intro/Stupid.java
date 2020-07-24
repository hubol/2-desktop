package world.intro;

import graphics.Graphics;
import graphics.Text;

import java.util.ArrayList;

import audio.Audio;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import main.Calc;
import main.Entity;

public class Stupid extends Entity{
	public String initText = "";
	public final String finalText = "HUBOL";
	public boolean[] changed;
	public int changeType, lChange, effect, effectMult;
	public Shake s;
	public int done;
	
	public double pitch, increase;
	public int sound;
	
	public double scale;

	public Stupid() {
		super(0, 0);
		changed = new boolean[5];
		s = new Shake();
		
		for (int i=0; i<5; i++)
			changed[i] = false;
		
		ArrayList<String> possible = new ArrayList<String>();
		possible.add("LOSER");
		possible.add("LOVER");
		possible.add("ZZZZZ");
		//possible.add("HAPPY");
		//possible.add("LOBUH"); 
		possible.add("BOGUS");
		possible.add("22222");
		//possible.add("FUNKY");
		possible.add("GREAT");
		possible.add("INTRO");
		possible.add("WORLD");
		//possible.add("QUEER");
		//possible.add("MUSIC");
		possible.add("ERR0R");
		possible.add("WACKY");
		possible.add("COCKS");
		possible.add("DICKS");
		possible.add("DONGS");
		possible.add("PENIS");
		possible.add("DADDY");
		possible.add("XXXXX");
		possible.add("FELIX");
		possible.add("CONDO");
		possible.add("JELLY");
		possible.add("DASSS");
		//possible.add("BLOOD");
		possible.add("URINE");
		possible.add("SEMEN");
		possible.add("SPUNK");
		possible.add("NO115");
		possible.add("WEEED");
		possible.add("QWEER");
		
		initText = possible.get((int)Intro.me.myGen.random(possible.size()));
		changeType = (int)Intro.me.myGen.random(3);
		lChange = -1;
		
		scale = 1;
		effect = (int)Intro.me.myGen.random(5);
		effectMult = (Integer)Intro.me.myGen.choose(1, -1);
		
		sound = (int)Intro.me.myGen.random(5);
		pitch = 1 + Calc.random(.5);
		increase = Calc.random(.2);
		
		done = 0;
		
		alarmInitialize(2);
		alarm[0] = 11;
	}
	
	public void step(){
		alarmStep();
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			int toDo = -1;
			if (changeType < 2){
				if (lChange == -1){
					toDo = 0;
					if (changeType == 1)
						toDo = 4;
				}
				else{
					if (changeType == 0)
						toDo = lChange + 1;
					else
						toDo = lChange - 1;
				}
			}
			else{
				toDo = (int)Intro.me.myGen.random(5);
				while (changed[toDo]){
					toDo += 1;
					if (toDo > 4)
						toDo = 0;
				}
			}
			
			Audio.get("sIntroStupid"+sound).setPitch(pitch);
			Sound.play("sIntroStupid"+sound);
			pitch += increase;
			
			changed[toDo] = true;
			lChange = toDo;
			done += 1;
			
			if (effect == 0)
				scale += .1 * effectMult;
			else if (effect == 1)
				angle += 20 * effectMult;
			else if (effect == 2)
				x += 16 * effectMult;
			else if (effect == 3)
				y += 16 * effectMult;
			else{
				x += 11 * effectMult;
				y += 11 *effectMult;
			}
			
			if (done == 5)
				alarm[1] = 60;
			else
				alarm[0] = 10;
		}
		else
			Intro.me.complete();
	}
	
	public void render(){
		String text = "";
		for (int i=0; i<5; i++){
			char s;
			if (changed[i])
				s = finalText.charAt(i);
			else
				s = initText.charAt(i);
			text = text+s;
		}
		
		Text.idiot = false;
		Text.randomize(.2);
		Graphics.setColor(Intro.me.LINE);
		Graphics.setAlpha(1);
		Text.setFont(Global.FONT);
		Text.orientation = Text.CENTERED;
		Text.drawTextExt(x + s.x + Calc.rangedRandom(.2) + 320, y + s.y + Calc.rangedRandom(.2) + 240, text, scale * 2, scale * 2, angle);
		Text.randomize(0);
		Text.idiot = true;
	}

}
