package world.junk;

import main.Calc;
import audio.Audio;
import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.enemy.BasicEnemy;

public class KissFaceUnder extends BasicEnemy{
	public boolean right, done;
	
	public double faceImg = 0, droolImg = 0;
	public KissFaceControl mom;
	
	public boolean prep = false, play = false;
	public String sound = "sAddRight";
	
	public Shake s = new Shake(.5);
	public boolean a = false;

	public KissFaceUnder(double x, double y, boolean r, boolean d, KissFaceControl mommy) {
		super(x, y);
		right = r;
		done = d;
		
		if (!done)
			faceImg = 2;
		
		if (!right){
			droolImg = 1;
			sound = "sAddLeft";
		}
		
		mom = mommy;
		
		mask = Global.sBLOCK.mask;
		setDepth(20);
		alarmInitialize(1);
		alarm[0] = 18 + (int)Calc.random(14);
	}
	
	public void alarmEvent(int i){
		if (!done)
			Sound.playPitched("sKissDrool", .2);
		
		if (droolImg == 0)
			droolImg = 1;
		else
			droolImg = 0;
		alarm[0] = 18 + (int)Calc.random(14);
	}
	
	public void step(){
		a = !a;
		
		if (!prep && Audio.soundExists(sound)){
			Audio.get(sound).setGain(0);
			Audio.fade(sound, 1, .03);
			Audio.get(sound).setLooping(true);
			prep = true;
		}
		
		if (done)
			faceImg -= .35;
		else
			faceImg += .35;
		
		faceImg = Math.max(0, Math.min(faceImg, 2));
		
		if ((int)faceImg == 1)
			droolImg = 0;
		
		if (prep && done && !play && (int)faceImg == 0){
			play = true;
			Sound.play(sound);
		}
		
		alarmStep();
	}
	
	public void gunDamage(){
		if (!done){
			done = mom.awaken(right);
			if (done)
				Sound.playPitched("sKissCongrats", .02);
		}
	}
	
	public void render(){
		final double scale = 32.0 / 48.0;
		final double dX = 19 - 16, dY = 24 - 16;
		double addX = 0;
		
		if ((int)faceImg == 0){
			addX = -1;
			if (a)
				addX = 1;
		}
		
		Sprite.get("sKissFace").render(faceImg, Sprite.CENTERED, x + s.x + Calc.rangedRandom(.2) + addX, y + s.y + Calc.rangedRandom(.2), scale, scale, 0, 1, 1, 1, 1);
		if ((int)faceImg > 0)
			Sprite.get("sKissDrool").render(droolImg, Sprite.NORTH, x + s.x + dX + Calc.rangedRandom(.1), y + s.y + dY + Calc.rangedRandom(.1), scale *.5, scale *.5, 0, 1, 1, 1, 1);
	}

}
