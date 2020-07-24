package world.event;

import graphics.Sprite;
import world.control.Global;
import world.control.Sound;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class DreamMusicBlock extends Entity {
	public double addY, toY, toXsc, toYsc;
	public boolean bounce;
	
	public int id, timer;

	public DreamMusicBlock(double x, double y, int i) {
		super(x, y);
		setCollisionGroup(Global.SOLIDBLOCK, Global.DEACTIVATEME);
		
		addY = 0;
		toY = 0;
		toXsc = 1;
		toYsc = 1;
		
		orientation = Sprite.NORTHWEST;
		mask = Global.sBLOCK.mask;
		
		bounce = false;
		
		id = i;
		timer = -1;
	}
	
	public void step(){
		if (timer > - 1)
			timer -= 1;
		
		if(bounce){
			toY += 1.6;
			toXsc -= .0365;
			toYsc += .03675;
			if (toY > 0)
				toY = 0;
			if (toXsc < 1)
				toXsc = 1;
			if (toYsc > 1)
				toYsc = 1;
			if (toY == 0 && toXsc == 1 && toYsc == 1)
				bounce = false;
		}
		
		double preXsc = xscale, preYsc = yscale;
		xscale = 1;
		yscale = 1;
		
		if (Player.me.vspeed <= 0 && Player.me.y <= y + 48 && timer == -1 && Scene.collision(this, x, y + 1, Global.PLAYER)){
			Sound.playPitched("sDreamBlock",.22);
			bounce = true;
			timer = 9;
			toY = -22;
			toXsc = 1.6;
			toYsc = .62;
			
			if (id < 4)
				DreamMusicControl.me.activated[DreamMusicControl.me.editSelect][id] = !DreamMusicControl.me.activated[DreamMusicControl.me.editSelect][id];
			else
				DreamMusicControl.me.confirm();
		}
		
		xscale = preXsc;
		yscale = preYsc;
		
		addY = Calc.approach(addY, toY, 2);
		xscale = Calc.approach(xscale, toXsc, 2.25);
		yscale = Calc.approach(yscale, toYsc, 2.5);
	}
	
	public void render(){
		Sprite.get("sMusicBlock").render(0, Sprite.CENTERED, x + 16 + Calc.rangedRandom(.7), y + addY + 16 + Calc.rangedRandom(.7), xscale, yscale, 0, 1, 1, 1, 1);
		Sprite.get("sMusicBoxIcons").render(id, Sprite.CENTERED, x + 16 + Calc.rangedRandom(.7), y + addY + 16 + Calc.rangedRandom(.7), xscale*.8, yscale*.8, 0, 1, 1, 1, 1);
	}

}
