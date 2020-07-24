package world.intro;

import graphics.Graphics;
import graphics.Shape;
import audio.Audio;
import world.Root;
import world.control.Global;
import world.control.Sound;
import main.Calc;
import main.Entity;

public class IntroCover extends Entity{
	public boolean[] draw;
	public int done;

	public IntroCover(double x, double y) {
		super(x, y);
		draw = new boolean[48];
		for (int i=0; i<48; i++)
			draw[i] = false;
		done = 0;
		alarmInitialize(2);
		sound();
		
		for (int i=0; i<10; i++)
			Audio.fade("sIntro"+i, 0, Intro.me.myGen.random(.1));
	}
	
	public void alarmEvent(int i){
		if (i == 0)
			sound();
		else
			Root.changeRoom("menu");
	}
	
	public void step(){
		alarmStep();
	}
	
	public void sound(){
		done += 1;
		
		Audio.get("sIntroCover").setPitch(1 + Intro.me.myGen.random(1));
		Sound.play("sIntroCover");
		
		for (int i=0; i<12; i++){
			int a = (int)Intro.me.myGen.random(48);
			while (draw[a]){
				a += 1;
				if (a > 47)
					a = 0;
			}
			draw[a] = true;
		}
		
		if (done == 4){
			alarm[1] = 30;
		}
		else
			alarm[0] = 15;
	}
	
	public void render(){
		Graphics.setAlpha(1);
		Graphics.setColor(Global.menuBackgroundColor);
		for (int i=0; i<8; i++){
			for (int j = 0; j<6; j++){
				if (draw[Calc.getGridPosition(i, j, 8)])
					Shape.drawRectangle(i * 80, j * 80, (i + 1) * 80, (j + 1) * 80);
			}
		}
	}

}
