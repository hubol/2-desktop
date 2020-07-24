package world.junk;

import graphics.Sprite;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.control.SpriteLoader;
import world.dream.DreamHeartParticles;
import world.gameplay.Upblock;
import main.Calc;
import main.Entity;

public class LovelessRock extends Entity{
	public Shake s = new Shake(.4);
	public Upblock mine;
	public boolean a = true, noSound = false;

	public LovelessRock(double x, double y) {
		super(x, y);
		new SpriteLoader("sLovelessRock_4");
		sprite = Sprite.get("sLovelessRock");
		orientation = Sprite.SOUTH;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		mine = new Upblock(x - 16, y - 32);
		
		if (Global.event[EV.LOVERCRACK] == 0)
			imageSingle = 0;
		else
			imageSingle = 2;
		
		alarmInitialize(2);
		alarm[0] = 1 + (int)Calc.random(90);
		
		setDepth(4);
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			if (!noSound)
				Sound.playPitched("sRockBlink");
			imageSingle += 1;
			alarm[1] = 3 + (int)Calc.random(3);
		}
		else if (i == 1){
			imageSingle -= 1;
			alarm[0] = 1 + (int)Calc.random(90);
		}
	}
	
	public void step(){
		a = !a;
		
		if (Global.event[EV.LOVERCRACK] == 1 && a)
			new DreamHeartParticles(x + Calc.rangedRandom(2), y - 36 - Calc.random(3.5), 90 + Calc.rangedRandom(7), 3);
		alarmStep();
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + s.x + Calc.rangedRandom(.2), y + s.y + Calc.rangedRandom(.2), .85, .85, 0, 1, 1, 1, 1);
	}

}
