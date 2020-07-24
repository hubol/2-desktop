package world.death;

import audio.Audio;
import world.control.Global;
import world.control.Sound;
import world.event.SmallMessage;
import world.particles.GlitchBlood;
import world.particles.JumpDreamPart;
import world.player.Damage;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Dead extends Entity{

	public Dead(double x, double y) {
		super(x, y);
		
		Audio.stopAll();
		Scene.destroy(SmallMessage.class);
		Scene.destroy(Damage.class);
		Scene.destroy(GlitchBlood.class);
		 
		Sound.playPitched("sDode", .05);
		
		sprite = Sprite.get("sDead");
		alarmInitialize(1);
		
		setDirSpeed(Calc.random(360), 8 + Calc.random(4));
		setDepth(Integer.MIN_VALUE + 8);
	}
	
	public void step(){
		setDirSpeed(getDirection() + 2, Math.max(0, (getSpeed() * .91) - .0785));
		x += Calc.rangedRandom(.4);
		y += Calc.rangedRandom(.4);
		
		if (getSpeed() < .1 && visible){
			Sound.playPitched("sMegaDode",.05);
			visible = false;
			
			for (int i=0; i<50; i++){
				JumpDreamPart j = new JumpDreamPart(x + Calc.rangedRandom(16.8), y + Calc.rangedRandom(16.8), "0081CF");
				j.setDepth(Integer.MIN_VALUE + 6);
				j.size = 2.9 + Calc.random(5);
				j.setDirSpeed(Calc.random(360), Calc.random(6));
			}
			for (int i=0; i<30; i++){
				JumpDreamPart j = new JumpDreamPart(x + Calc.rangedRandom(13), y + Calc.rangedRandom(13), "FFFFFF");
				j.setDepth(Integer.MIN_VALUE + 6);
				j.size = 2.5 + Calc.random(3);
				j.setDirSpeed(Calc.random(360), Calc.random(6));
			}
			
			alarm[0] = 90;
		}
		
		super.step();
	}
	
	public void alarmEvent(int i){
		Global.toDeath();
	}

}
