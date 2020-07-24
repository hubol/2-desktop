package world.intro;

import graphics.Sprite;
import main.Entity;

public class Fall extends Entity{

	public Fall() {
		super(0, 0);
		imageSingle = (int)Intro.me.myGen.random(3);
		imageSpeed = 0;
		alarmInitialize(6);
		alarm[0] = 15;
		
		setDepth(2);
	}
	
	public void alarmEvent(int i){
		if (i < 5){
			//140 + (i * 88)
			new FallLetter(i, 140 + (i * 88));
			alarm[i + 1] = 15;
			if (i == 4)
				alarm[5] = 90;
		}
		else
			Intro.me.complete();
	}
	
	public void step(){
		alarmStep();
	}
	
	public void render(){
		Sprite.get("sIntroGrass").render(imageSingle, Sprite.NORTHWEST, 0, 0, 1, 1, 0, 1, Intro.me.LINE);
	}

}
