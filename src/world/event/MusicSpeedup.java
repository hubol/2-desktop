package world.event;

import audio.Audio;
import main.Calc;
import graphics.Sprite;
import world.Fg;
import world.control.Global;
import world.enemy.BasicEnemy;

public class MusicSpeedup extends BasicEnemy{
	public double pitch;
	public int timer;

	public MusicSpeedup(double x, double y) {
		super(x, y);
		visible = false;
		orientation = Sprite.NORTHWEST;
		
		sprite = Global.sBLOCK;
		mask = sprite.mask;
		
		pitch = 1.0;
		timer = -1;
	}
	
	public void step(){
		pitch = Calc.approach(pitch, 1.0, 20);
		if (Audio.soundExists(Global.roomMusic))
			Audio.get(Global.roomMusic).setPitch(pitch);
		
		if (timer > -1)
			timer -= 1;
	}
	
	public void landDamage(){
		
	}
	
	public void gunDamage(){
		speedup();
	}
	
	public void bombDamage(){
		speedup();
	}
	
	public void speedup(){
		if (timer <= -1){
			for (int i=0; i<4; i++)
				Global.squareParticle(x + 16 + Calc.rangedRandom(16), y + 16 + Calc.rangedRandom(16), 3 + (int)Calc.random(5), "007050", 1.5 + Calc.random(5));
			
			Fg.me.shakeTimer = 10;
			pitch = 5.0;
			Audio.get(Global.roomMusic).setPitch(pitch);
			
			timer = 5;
		}
	}
	
	public void roomDestroy(){
		if (Audio.soundExists(Global.roomMusic))
			Audio.get(Global.roomMusic).setPitch(1);
		super.roomDestroy();
	}

}
