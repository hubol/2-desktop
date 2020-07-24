package world.intro;

import audio.Audio;
import world.control.Shake;
import world.control.Sound;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class FallLetter extends Entity{
	public Shake s;

	public FallLetter(int img, double x) {
		super(x, 0);
		sprite = Sprite.get("sIntroHubolFallLine");
		orientation = Sprite.SOUTH;
		imageSingle = img;
		imageSpeed = 0;
		vspeed = .1;
		
		s = new Shake(.2);
	}
	
	public void step(){
		if (vspeed != 0){
			vspeed += .8;
			vspeed *= 1.008;
		}
		super.step();
		if (y >= 375 && vspeed > 0){
			vspeed = 0;
			y = 375;
			Audio.get("sIntroFallLand").setPitch(1 + (.5 * ((double)imageSingle / 4.0)) + Intro.me.myGen.rangedRandom(.05));
			Sound.play("sIntroFallLand");
			new FallDust(x, y, 1);
			new FallDust(x, y, -1);
		}
	}
	
	public void render(){
		double x = this.x + s.x + Calc.rangedRandom(.2), y = this.y + s.y + Calc.rangedRandom(.2);
		Sprite.get("sIntroHubolFallLine").render(imageSingle, orientation, x, y, 1, 1, 0, 1, Intro.me.LINE);
		Sprite.get("sIntroHubolFallBack").render(imageSingle, orientation, x, y, 1, 1, 0, 1, Intro.me.BACK);
	}

}
