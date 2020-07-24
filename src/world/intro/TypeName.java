package world.intro;

import graphics.Graphics;
import graphics.Text;
import world.control.Global;
import world.control.Sound;
import audio.Audio;
import main.Entity;

public class TypeName extends Entity{
	public double pitch;
	public int timer, done;
	
	public String txt = "";
	public boolean yes = true;

	public TypeName() {
		super(0, 0);
		pitch = 1 + Intro.me.myGen.rangedRandom(.25);
		done = 0;
		
		chime();
		alarmInitialize(1);
	}
	
	public void alarmEvent(int i){
		Intro.me.complete();
	}
	
	public void chime(){
		yes = !yes;
		
		String s = "sIntroT"+(int)Intro.me.myGen.random(6);
		Audio.get(s).setPitch(pitch);
		Sound.play(s);
		
		txt += "HUBOL";
		if (yes)
			txt += "%";
		
		done += 1;
		if (done < 20){
			timer = 6;
		}
		else{
			timer = -1;
			alarm[0] = 60;
		}
	}
	
	public void step(){
		timer -= 1;
		if (timer == 0){
			chime();
		}
		
		alarmStep();
	}
	
	public void render(){
		Graphics.setColor(Intro.me.LINE);
		Graphics.setAlpha(1);
		Text.setFont(Global.FONT);
		Text.randomize(.1);
		Text.orientation = Text.NORTHWEST;
		Text.drawTextExt(0, 0, txt, 2.0, 48.0 / 32.0, 0);
		Text.randomize(0);
	}

}
