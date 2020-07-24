package world.gameplay;

import java.util.ArrayList;

import world.control.Global;
import world.control.IO;
import world.control.Sound;
import world.event.NoOccupy;
import world.junk.StupidHeartEvent;
import world.particles.JumpDreamPart;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class StupidBlock extends Entity{
	public boolean killed = false;
	private double multiplier = 1;

	public StupidBlock(double x, double y, double angle) {
		super(x, y);
		this.angle = angle;
		
		orientation = Sprite.NORTHWEST;
		mask = Global.sBLOCK.mask;
		imageSpeed = 0;
		
		setCollisionGroup(Global.DEACTIVATEME, Global.SOLIDBLOCK, Global.TEMPSOLID);
		setDepth(0);
	}
	
	public void check(){
		if ((Player.me.y >= y - 14 && Player.me.y <= y + 46 && ((Player.me.x >= x - 20 && Player.me.x <= x - 14 && angle == 0 && Player.me.hspeed > 0)||(Player.me.x >= x + 47 && Player.me.x <= x + 50 && angle == 180 && Player.me.hspeed < 0)))||(Player.me.x >= x - 14 && Player.me.x <= x + 46 && ((Player.me.y >= y - 18 && Player.me.y <= y - 15 && angle == 270 && Player.me.vspeed > 0)||(Player.me.y >= y + 47 && Player.me.y <= y + 50 && angle == 90 && (Player.me.vspeed < 0 || IO.checkKey(Global.JUMP))))))
			release();
	}
	
	public void step(){
		if (!killed){
			check();
		}
		else{
			super.step();
			setDirSpeed(angle, getSpeed() * 1.025);
			if (multiplier > 0)
				multiplier -= .011;
			
			if (x >= 640 || x <= - 32 || y <= -32 || y >= 480)
				destroy();
			
			new JumpDreamPart(x + Calc.random(32), y + Calc.random(32), "FF1879");
			for (int i=0; i<2; i++)
				new JumpDreamPart(x + Calc.random(32), y + Calc.random(32), "D7FF18");
		}
	}
	
	public void release(){
		if (!killed){
			new NoOccupy(x, y);
			
			setDepth(-1);
			setCollisionGroup(Global.DEACTIVATEME);
			killed = true;
			Sound.playPitched("sStupidBlockKill");
			setDirSpeed(angle, 16);
			
			ArrayList<StupidHeartEvent> ev = Scene.getEntityList(StupidHeartEvent.class);
			for (int i=0; i<ev.size(); i++)
				ev.get(i).update();
		}
	}
	
	public void render(){
		
	}
	
	public void back(Sprite s, double xx, double yy){
		s.render(0, Sprite.CENTERED, x + 16 + xx, y + 16 + yy, .52 * multiplier, .52 * multiplier, 0, 1, 1, 1, 1);
	}
	
	public void render(Sprite sprite, double xx, double yy){
		sprite.render(0, Sprite.CENTERED, x + 16 + xx, y + 16 + yy, .5 * multiplier, .5 * multiplier, angle, 1, 1, 1, 1);
	}

}
